package com.wanghui.shiyue.todo.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.wanghui.shiyue.comm.codes.BaseCode;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import com.wanghui.shiyue.comm.utils.IdGenerator;
import com.wanghui.shiyue.todo.convert.SubTaskConvert;
import com.wanghui.shiyue.todo.convert.TodoConvert;
import com.wanghui.shiyue.todo.dao.SubTaskMapper;
import com.wanghui.shiyue.todo.dao.TodoLogMapper;
import com.wanghui.shiyue.todo.dao.TodoMapper;
import com.wanghui.shiyue.todo.entity.dto.SubTaskDTO;
import com.wanghui.shiyue.todo.entity.dto.TodoDTO;
import com.wanghui.shiyue.todo.entity.dto.TodoQueryParam;
import com.wanghui.shiyue.todo.entity.po.SubTaskPO;
import com.wanghui.shiyue.todo.entity.po.TodoLogPO;
import com.wanghui.shiyue.todo.entity.po.TodoPO;
import com.wanghui.shiyue.todo.service.TodoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TodoServiceImpl implements TodoService {
    @Resource
    private TodoMapper todoMapper;
    @Resource
    private SubTaskMapper subTaskMapper;
    @Resource
    private TodoLogMapper todoLogMapper;
    @Resource
    private TodoConvert todoConvert;
    @Resource
    private SubTaskConvert subTaskConvert;

    @Override
    public List<TodoDTO> query(TodoQueryParam param) {
        List<TodoPO> pos = todoMapper.queryByFilters(param.getCompleted(), param.getPriority(), param.getStartTime(), param.getEndTime(), param.getOrderByPriority());
        List<TodoDTO> dtos = todoConvert.posToDto(pos);
        for (TodoDTO dto : dtos) {
            dto.setSubTasks(listSubTasks(dto.getTodoId()));
        }
        return dtos;
    }

    @Override
    @Transactional
    public TodoDTO save(TodoDTO dto) {
        validate(dto);
        TodoPO po = todoConvert.dtoToPo(dto);
        po.setTodoId(IdGenerator.generator());
        po.init();
        todoMapper.insert(po);
        if (CollUtil.isNotEmpty(dto.getSubTasks())) {
            for (SubTaskDTO s : dto.getSubTasks()) {
                SubTaskPO spo = subTaskConvert.dtoToPo(s);
                spo.setSubTaskId(IdGenerator.generator());
                spo.setTodoId(po.getTodoId());
                spo.init();
                subTaskMapper.insert(spo);
            }
        }
        recordLog("create", po.getTodoId(), "创建待办", "success");
        return detail(po.getTodoId());
    }

    @Override
    @Transactional
    public TodoDTO update(TodoDTO dto) {
        if (dto.getTodoId() == null) {
            throw new IllegalArgumentException("todoId不能为空");
        }
        validate(dto);
        TodoPO po = todoConvert.dtoToPo(dto);
        po.init();
        todoMapper.updateById(po);
        if (dto.getSubTasks() != null) {
            for (SubTaskDTO s : dto.getSubTasks()) {
                if (s.getSubTaskId() == null) {
                    SubTaskPO spo = subTaskConvert.dtoToPo(s);
                    spo.setSubTaskId(IdGenerator.generator());
                    spo.setTodoId(dto.getTodoId());
                    spo.init();
                    subTaskMapper.insert(spo);
                } else {
                    SubTaskPO spo = subTaskConvert.dtoToPo(s);
                    spo.init();
                    subTaskMapper.updateById(spo);
                }
            }
        }
        recordLog("update", dto.getTodoId(), "更新待办", "success");
        return detail(dto.getTodoId());
    }

    @Override
    @Transactional
    public Boolean delete(Long todoId) {
        if (todoId == null) {
            throw new IllegalArgumentException("todoId不能为空");
        }
        List<SubTaskPO> subs = subTaskMapper.listByTodoId(todoId);
        for (SubTaskPO s : subs) {
            subTaskMapper.deleteById(s.getSubTaskId());
        }
        int r = todoMapper.deleteById(todoId);
        recordLog("delete", todoId, "删除待办", r > 0 ? "success" : "fail");
        return r > 0;
    }

    @Override
    @Transactional
    public Boolean deleteBatch(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new IllegalArgumentException("ids不能为空");
        }
        for (Long id : ids) {
            delete(id);
        }
        recordLog("deleteBatch", null, "批量删除待办", "success");
        return true;
    }

    @Override
    public List<SubTaskDTO> listSubTasks(Long todoId) {
        List<SubTaskPO> list = subTaskMapper.listByTodoId(todoId);
        Map<Long, List<SubTaskDTO>> childrenMap = new HashMap<>();
        List<SubTaskDTO> nodes = subTaskConvert.posToDto(list);
        for (SubTaskDTO n : nodes) {
            Long p = n.getParentId();
            if (p != null) {
                childrenMap.computeIfAbsent(p, k -> new ArrayList<>()).add(n);
            }
        }
        Map<Long, SubTaskDTO> map = nodes.stream().collect(Collectors.toMap(SubTaskDTO::getSubTaskId, v -> v));
        List<SubTaskDTO> roots = new ArrayList<>();
        for (SubTaskDTO n : nodes) {
            List<SubTaskDTO> c = childrenMap.get(n.getSubTaskId());
            if (c != null) {
                n.setChildren(c);
            }
            if (n.getParentId() == null) {
                roots.add(n);
            }
        }
        return roots;
    }

    @Override
    @Transactional
    public SubTaskDTO addSubTask(SubTaskDTO dto) {
        if (dto.getTodoId() == null || StringUtils.isBlank(dto.getName())) {
            throw new IllegalArgumentException("参数不合法");
        }
        dto.setName(dto.getName().trim());
        if (dto.getName().length() > 255) {
            throw new IllegalArgumentException("子项目名称长度不能超过255");
        }
        SubTaskPO po = subTaskConvert.dtoToPo(dto);
        po.setSubTaskId(IdGenerator.generator());
        po.init();
        subTaskMapper.insert(po);
        recordLog("subtask_add", dto.getTodoId(), "新增子项目", "success");
        return subTaskConvert.poToDto(po);
    }

    @Override
    @Transactional
    public SubTaskDTO updateSubTask(SubTaskDTO dto) {
        if (dto.getSubTaskId() == null) {
            throw new IllegalArgumentException("subTaskId不能为空");
        }
        SubTaskPO po = subTaskConvert.dtoToPo(dto);
        po.init();
        subTaskMapper.updateById(po);
        recordLog("subtask_update", dto.getTodoId(), "更新子项目", "success");
        return subTaskConvert.poToDto(po);
    }

    @Override
    @Transactional
    public Boolean deleteSubTask(Long subTaskId) {
        if (subTaskId == null) {
            throw new IllegalArgumentException("subTaskId不能为空");
        }
        List<SubTaskPO> children = subTaskMapper.listByParentId(subTaskId);
        for (SubTaskPO c : children) {
            deleteSubTask(c.getSubTaskId());
        }
        int r = subTaskMapper.deleteById(subTaskId);
        recordLog("subtask_delete", subTaskId, "删除子项目", r > 0 ? "success" : "fail");
        return r > 0;
    }

    private TodoDTO detail(Long todoId) {
        TodoPO po = todoMapper.selectById(todoId);
        TodoDTO dto = todoConvert.poToDto(po);
        dto.setSubTasks(listSubTasks(todoId));
        return dto;
    }

    private void validate(TodoDTO dto) {
        if (StringUtils.isBlank(dto.getTitle())) {
            throw new IllegalArgumentException("标题不能为空");
        }
        dto.setTitle(dto.getTitle().trim());
        if (dto.getRemark() != null) {
            String s = dto.getRemark().trim();
            if (s.length() > 500) {
                throw new IllegalArgumentException("备注长度不能超过500");
            }
            dto.setRemark(s);
        }
        if (dto.getDueTime() == null) {
            throw new IllegalArgumentException("截止时间不能为空");
        }
        if (dto.getPriority() == null) {
            throw new IllegalArgumentException("优先级不能为空");
        }
        if (!(dto.getPriority() == 1 || dto.getPriority() == 2 || dto.getPriority() == 3)) {
            throw new IllegalArgumentException("优先级必须为1/2/3");
        }
    }

    private void recordLog(String action, Long targetId, String content, String result) {
        TodoLogPO logPo = new TodoLogPO();
        logPo.setLogId(IdGenerator.generator());
        logPo.setAction(action);
        logPo.setTargetId(targetId);
        logPo.setContent(content);
        logPo.setResult(result);
        logPo.init();
        todoLogMapper.insert(logPo);
    }
}
