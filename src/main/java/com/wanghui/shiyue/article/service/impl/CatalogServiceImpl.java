package com.wanghui.shiyue.article.service.impl;

import com.wanghui.shiyue.article.convert.CatalogConvert;
import com.wanghui.shiyue.article.dao.CatalogMapper;
import com.wanghui.shiyue.article.entity.dto.CatalogDTO;
import com.wanghui.shiyue.article.entity.po.CatalogPO;
import com.wanghui.shiyue.article.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目录服务实现类
 * @description: 实现目录的CRUD操作和层级关系处理功能
 * @fileName: CatalogServiceImpl
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {

    @Resource
    private CatalogMapper catalogMapper;

    @Resource
    private CatalogConvert catalogConvert;

    @Override
    public CatalogDTO getById(Long catalogId) {
        CatalogPO catalogPO = catalogMapper.selectById(catalogId);
        return catalogConvert.poToDto(catalogPO);
    }

    @Override
    public List<CatalogDTO> getAllCatalogs() {
        List<CatalogPO> catalogPOS = catalogMapper.selectAllOrdered();
        return catalogConvert.posToDto(catalogPOS);
    }

    @Override
    public List<CatalogDTO> getCatalogTree() {
        List<CatalogPO> catalogPOS = catalogMapper.selectAllOrdered();
        List<CatalogDTO> catalogDTOS = catalogConvert.posToDto(catalogPOS);
        return buildCatalogTree(catalogDTOS);
    }

    @Override
    public List<CatalogDTO> getChildrenByParentId(Long parentId) {
        List<CatalogPO> catalogPOS = catalogMapper.selectByParentId(parentId);
        return catalogConvert.posToDto(catalogPOS);
    }

    @Override
    @Transactional
    public Boolean save(CatalogDTO catalogDTO) {
        // 验证目录名称
        if (StringUtils.isBlank(catalogDTO.getName())) {
            throw new IllegalArgumentException("目录名称不能为空");
        }

        // 构建目录路径
        if (catalogDTO.getParentId() == null || catalogDTO.getParentId() == 0) {
            // 顶级目录
            catalogDTO.setLevel(1);
            catalogDTO.setPath("1"); // 暂时使用简单的路径，实际应该根据最大id生成
        } else {
            // 子目录
            CatalogPO parentCatalog = catalogMapper.selectById(catalogDTO.getParentId());
            if (parentCatalog == null) {
                throw new IllegalArgumentException("父目录不存在");
            }
            catalogDTO.setLevel(parentCatalog.getLevel() + 1);
            catalogDTO.setPath(parentCatalog.getPath() + "/" + (catalogMapper.selectCount(null) + 1)); // 暂时使用简单的路径生成
        }

        // 转换为PO并保存
        CatalogPO catalogPO = catalogConvert.dtoToPo(catalogDTO);
        return catalogMapper.insert(catalogPO) > 0;
    }

    @Override
    @Transactional
    public Boolean update(CatalogDTO catalogDTO) {
        // 验证目录存在
        CatalogPO existingCatalog = catalogMapper.selectById(catalogDTO.getCatalogId());
        if (existingCatalog == null) {
            throw new IllegalArgumentException("目录不存在");
        }

        // 验证目录名称
        if (StringUtils.isBlank(catalogDTO.getName())) {
            throw new IllegalArgumentException("目录名称不能为空");
        }

        // 更新目录信息
        CatalogPO catalogPO = catalogConvert.dtoToPo(catalogDTO);
        return catalogMapper.updateById(catalogPO) > 0;
    }

    @Override
    @Transactional
    public Boolean delete(Long catalogId) {
        // 验证目录存在
        CatalogPO catalogPO = catalogMapper.selectById(catalogId);
        if (catalogPO == null) {
            throw new IllegalArgumentException("目录不存在");
        }

        // 检查是否有子目录
        List<CatalogPO> children = catalogMapper.selectByParentId(catalogId);
        if (!children.isEmpty()) {
            throw new IllegalArgumentException("目录下存在子目录，无法删除");
        }

        // 逻辑删除目录
        catalogPO.setInvalidFlag("1");
        return catalogMapper.updateById(catalogPO) > 0;
    }

    @Override
    public CatalogDTO getByPath(String path) {
        CatalogPO catalogPO = catalogMapper.selectByPath(path);
        return catalogConvert.poToDto(catalogPO);
    }

    @Override
    public List<CatalogDTO> getByLevel(Integer level) {
        List<CatalogPO> catalogPOS = catalogMapper.selectByLevel(level);
        return catalogConvert.posToDto(catalogPOS);
    }

    /**
     * 构建目录树状结构
     * @param catalogDTOS 目录DTO列表
     * @return {@link List }<{@link CatalogDTO }>
     */
    private List<CatalogDTO> buildCatalogTree(List<CatalogDTO> catalogDTOS) {
        Map<Long, CatalogDTO> catalogMap = new HashMap<>();
        List<CatalogDTO> rootCatalogs = new ArrayList<>();

        // 构建目录映射
        for (CatalogDTO catalogDTO : catalogDTOS) {
            catalogMap.put(catalogDTO.getCatalogId(), catalogDTO);
            catalogDTO.setChildren(new ArrayList<>());
        }

        // 构建目录树
        for (CatalogDTO catalogDTO : catalogDTOS) {
            if (catalogDTO.getParentId() == null || catalogDTO.getParentId() == 0) {
                // 顶级目录
                rootCatalogs.add(catalogDTO);
            } else {
                // 子目录
                CatalogDTO parentCatalog = catalogMap.get(catalogDTO.getParentId());
                if (parentCatalog != null) {
                    parentCatalog.getChildren().add(catalogDTO);
                }
            }
        }

        return rootCatalogs;
    }
}
