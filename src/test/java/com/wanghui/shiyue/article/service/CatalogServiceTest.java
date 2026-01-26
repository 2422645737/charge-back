package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.dto.CatalogDTO;
import com.wanghui.shiyue.article.entity.po.CatalogPO;
import com.wanghui.shiyue.article.service.impl.CatalogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CatalogService测试类
 * @description: 测试CatalogService的核心功能，包括目录的保存、更新、删除以及目录树的构建等
 * @fileName: CatalogServiceTest
 * @author: wanghui
 * @createAt: 2026/01/25 11:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @InjectMocks
    private CatalogServiceImpl catalogService;

    @Mock
    private com.wanghui.shiyue.article.dao.CatalogMapper catalogMapper;

    @Mock
    private com.wanghui.shiyue.article.convert.CatalogConvert catalogConvert;

    private CatalogDTO catalogDTO;
    private CatalogPO catalogPO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        catalogDTO = new CatalogDTO();
        catalogDTO.setCatalogId(1L);
        catalogDTO.setParentId(0L);
        catalogDTO.setName("测试目录");
        catalogDTO.setLevel(1);
        catalogDTO.setPath("/1");

        catalogPO = new CatalogPO();
        catalogPO.setCatalogId(1L);
        catalogPO.setParentId(0L);
        catalogPO.setName("测试目录");
        catalogPO.setLevel(1);
        catalogPO.setPath("/1");
        catalogPO.setInvalidFlag("0");
    }

    /**
     * 测试保存目录
     */
    @Test
    void testSaveCatalog() {
        // 模拟方法调用
        when(catalogConvert.dtoToPo(catalogDTO)).thenReturn(catalogPO);
        when(catalogMapper.insert(catalogPO)).thenReturn(1);

        // 执行测试
        Boolean result = catalogService.save(catalogDTO);

        // 验证结果
        assertTrue(result);
        verify(catalogConvert, times(1)).dtoToPo(catalogDTO);
        verify(catalogMapper, times(1)).insert(catalogPO);
    }

    /**
     * 测试更新目录
     */
    @Test
    void testUpdateCatalog() {
        // 设置测试数据
        catalogDTO.setName("更新后的目录");

        // 模拟方法调用
        when(catalogConvert.dtoToPo(catalogDTO)).thenReturn(catalogPO);
        when(catalogMapper.updateById(catalogPO)).thenReturn(1);

        // 执行测试
        Boolean result = catalogService.update(catalogDTO);

        // 验证结果
        assertTrue(result);
        verify(catalogConvert, times(1)).dtoToPo(catalogDTO);
        verify(catalogMapper, times(1)).updateById(catalogPO);
    }

    /**
     * 测试删除目录
     */
    @Test
    void testDeleteCatalog() {
        // 模拟方法调用
        when(catalogMapper.deleteById(1L)).thenReturn(1);

        // 执行测试
        Boolean result = catalogService.delete(1L);

        // 验证结果
        assertTrue(result);
        verify(catalogMapper, times(1)).deleteById(1L);
    }

    /**
     * 测试构建目录树
     */
    @Test
    void testBuildCatalogTree() {
        // 准备测试数据
        List<CatalogPO> catalogPOS = new ArrayList<>();
        // 根目录
        CatalogPO rootPO = new CatalogPO();
        rootPO.setCatalogId(1L);
        rootPO.setParentId(0L);
        rootPO.setName("根目录");
        rootPO.setLevel(1);
        rootPO.setPath("/1");
        rootPO.setInvalidFlag("0");
        catalogPOS.add(rootPO);
        // 子目录
        CatalogPO childPO = new CatalogPO();
        childPO.setCatalogId(2L);
        childPO.setParentId(1L);
        childPO.setName("子目录");
        childPO.setLevel(2);
        childPO.setPath("/1/2");
        childPO.setInvalidFlag("0");
        catalogPOS.add(childPO);

        // 模拟DTO转换
        List<CatalogDTO> catalogDTOS = new ArrayList<>();
        CatalogDTO rootDTO = new CatalogDTO();
        rootDTO.setCatalogId(1L);
        rootDTO.setParentId(0L);
        rootDTO.setName("根目录");
        rootDTO.setLevel(1);
        rootDTO.setPath("/1");
        catalogDTOS.add(rootDTO);
        CatalogDTO childDTO = new CatalogDTO();
        childDTO.setCatalogId(2L);
        childDTO.setParentId(1L);
        childDTO.setName("子目录");
        childDTO.setLevel(2);
        childDTO.setPath("/1/2");
        catalogDTOS.add(childDTO);

        // 模拟方法调用
        when(catalogMapper.selectList(any())).thenReturn(catalogPOS);
        when(catalogConvert.posToDto(catalogPOS)).thenReturn(catalogDTOS);

        // 执行测试
        List<CatalogDTO> result = catalogService.getCatalogTree();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size()); // 只返回根目录，子目录在children中
        CatalogDTO rootResult = result.get(0);
        assertEquals("根目录", rootResult.getName());
        assertNotNull(rootResult.getChildren());
        assertEquals(1, rootResult.getChildren().size()); // 根目录有一个子目录
        assertEquals("子目录", rootResult.getChildren().get(0).getName());
    }

    /**
     * 测试通过ID获取目录
     */
    @Test
    void testGetCatalogById() {
        // 模拟方法调用
        when(catalogMapper.selectById(1L)).thenReturn(catalogPO);
        when(catalogConvert.poToDto(catalogPO)).thenReturn(catalogDTO);

        // 执行测试
        CatalogDTO result = catalogService.getById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试目录", result.getName());
        verify(catalogMapper, times(1)).selectById(1L);
        verify(catalogConvert, times(1)).poToDto(catalogPO);
    }

    /**
     * 测试通过父ID获取子目录
     */
    @Test
    void testGetChildrenByParentId() {
        // 准备测试数据
        List<CatalogPO> childPOS = new ArrayList<>();
        CatalogPO childPO = new CatalogPO();
        childPO.setCatalogId(2L);
        childPO.setParentId(1L);
        childPO.setName("子目录");
        childPO.setLevel(2);
        childPO.setPath("/1/2");
        childPO.setInvalidFlag("0");
        childPOS.add(childPO);

        // 模拟DTO转换
        List<CatalogDTO> childDTOS = new ArrayList<>();
        CatalogDTO childDTO = new CatalogDTO();
        childDTO.setCatalogId(2L);
        childDTO.setParentId(1L);
        childDTO.setName("子目录");
        childDTO.setLevel(2);
        childDTO.setPath("/1/2");
        childDTOS.add(childDTO);

        // 模拟方法调用
        when(catalogMapper.selectByParentId(1L)).thenReturn(childPOS);
        when(catalogConvert.posToDto(childPOS)).thenReturn(childDTOS);

        // 执行测试
        List<CatalogDTO> result = catalogService.getChildrenByParentId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("子目录", result.get(0).getName());
        verify(catalogMapper, times(1)).selectByParentId(1L);
        verify(catalogConvert, times(1)).posToDto(childPOS);
    }
}
