package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.po.ArticleCatalogPO;
import com.wanghui.shiyue.article.service.impl.ArticleCatalogServiceImpl;
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
 * ArticleCatalogService测试类
 * @description: 测试ArticleCatalogService的核心功能，包括保存文章目录关联、删除文章目录关联等
 * @fileName: ArticleCatalogServiceTest
 * @author: wanghui
 * @createAt: 2026/01/25 12:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@ExtendWith(MockitoExtension.class)
class ArticleCatalogServiceTest {

    @InjectMocks
    private ArticleCatalogServiceImpl articleCatalogService;

    @Mock
    private com.wanghui.shiyue.article.dao.ArticleCatalogMapper articleCatalogMapper;

    @Mock
    private com.wanghui.shiyue.article.convert.ArticleCatalogConvert articleCatalogConvert;

    private ArticleCatalogPO articleCatalogPO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        articleCatalogPO = new ArticleCatalogPO();
        articleCatalogPO.setArticleCatalogId(1L);
        articleCatalogPO.setArticleId(1L);
        articleCatalogPO.setCatalogId(1L);
        articleCatalogPO.setCatalogName("测试目录");
    }

    /**
     * 测试保存文章目录关联
     */
    @Test
    void testSaveArticleCatalogs() {
        // 设置测试数据
        Long articleId = 1L;
        List<Long> catalogIds = List.of(1L, 2L, 3L);

        // 模拟方法调用
        when(articleCatalogMapper.deleteByArticleId(articleId)).thenReturn(3);
        when(articleCatalogMapper.insert(any(ArticleCatalogPO.class))).thenReturn(1);

        // 执行测试
        Boolean result = articleCatalogService.saveArticleCatalogs(articleId, catalogIds);

        // 验证结果
        assertTrue(result);
        verify(articleCatalogMapper, times(1)).deleteByArticleId(articleId);
        verify(articleCatalogMapper, times(3)).insert(any(ArticleCatalogPO.class));
    }

    /**
     * 测试删除文章目录关联
     */
    @Test
    void testDeleteByArticleId() {
        // 设置测试数据
        Long articleId = 1L;

        // 模拟方法调用
        when(articleCatalogMapper.deleteByArticleId(articleId)).thenReturn(2);

        // 执行测试
        Boolean result = articleCatalogService.deleteByArticleId(articleId);

        // 验证结果
        assertTrue(result);
        verify(articleCatalogMapper, times(1)).deleteByArticleId(articleId);
    }

    /**
     * 测试根据目录ID删除文章目录关联
     */
    @Test
    void testDeleteByCatalogId() {
        // 设置测试数据
        Long catalogId = 1L;

        // 模拟方法调用
        when(articleCatalogMapper.deleteByCatalogId(catalogId)).thenReturn(3);

        // 执行测试
        Boolean result = articleCatalogService.deleteByCatalogId(catalogId);

        // 验证结果
        assertTrue(result);
        verify(articleCatalogMapper, times(1)).deleteByCatalogId(catalogId);
    }

    /**
     * 测试根据文章ID获取目录
     */
    @Test
    void testGetByArticleId() {
        // 设置测试数据
        Long articleId = 1L;
        List<ArticleCatalogPO> articleCatalogPOS = new ArrayList<>();
        articleCatalogPOS.add(articleCatalogPO);

        // 创建期望的DTO结果
        List<com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO> expectedResult = new ArrayList<>();
        com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO expectedDTO = new com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO();
        expectedDTO.setArticleCatalogId(1L);
        expectedDTO.setArticleId(1L);
        expectedDTO.setCatalogId(1L);
        expectedDTO.setCatalogName("测试目录");
        expectedResult.add(expectedDTO);

        // 模拟方法调用
        when(articleCatalogMapper.selectByArticleId(articleId)).thenReturn(articleCatalogPOS);
        when(articleCatalogConvert.posToDto(articleCatalogPOS)).thenReturn(expectedResult);

        // 执行测试
        List<com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO> result = articleCatalogService.getByArticleId(articleId);

        System.out.println("测试结果: " + result);
        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // 验证返回结果中包含catalogName字段
        assertEquals("测试目录", result.get(0).getCatalogName());
        verify(articleCatalogMapper, times(1)).selectByArticleId(articleId);
        verify(articleCatalogConvert, times(1)).posToDto(articleCatalogPOS);
    }

    /**
     * 测试根据目录ID获取文章
     */
    @Test
    void testGetByCatalogId() {
        // 设置测试数据
        Long catalogId = 1L;
        List<ArticleCatalogPO> articleCatalogPOS = new ArrayList<>();
        articleCatalogPOS.add(articleCatalogPO);

        // 创建期望的DTO结果
        List<com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO> expectedResult = new ArrayList<>();
        com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO expectedDTO = new com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO();
        expectedDTO.setArticleCatalogId(1L);
        expectedDTO.setArticleId(1L);
        expectedDTO.setCatalogId(1L);
        expectedDTO.setCatalogName("测试目录");
        expectedResult.add(expectedDTO);

        // 模拟方法调用
        when(articleCatalogMapper.selectByCatalogId(catalogId)).thenReturn(articleCatalogPOS);
        when(articleCatalogConvert.posToDto(articleCatalogPOS)).thenReturn(expectedResult);

        // 执行测试
        List<com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO> result = articleCatalogService.getByCatalogId(catalogId);

        System.out.println("测试结果: " + result);
        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // 验证返回结果中包含catalogName字段
        assertEquals("测试目录", result.get(0).getCatalogName());
        verify(articleCatalogMapper, times(1)).selectByCatalogId(catalogId);
        verify(articleCatalogConvert, times(1)).posToDto(articleCatalogPOS);
    }
}
