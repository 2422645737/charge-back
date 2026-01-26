package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.service.impl.ArticleServiceImpl;
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
 * ArticleService测试类
 * @description: 测试ArticleService的save方法，覆盖主要业务逻辑路径和边界情况
 * @fileName: ArticleServiceTest
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Mock
    private com.wanghui.shiyue.article.dao.ArticleMapper articleMapper;

    @Mock
    private com.wanghui.shiyue.article.convert.ArticleConvert articleConvert;

    @Mock
    private TagService tagService;

    @Mock
    private ArticleTagService articleTagService;

    @Mock
    private com.wanghui.shiyue.article.service.ArticleCatalogService articleCatalogService;

    private ArticleDTO articleDTO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        articleDTO = new ArticleDTO();
        articleDTO.setTitle("测试文章");
        articleDTO.setContent("测试内容");
        articleDTO.setClassId(1L);
    }

    /**
     * 测试正常保存文章
     */
    @Test
    void testSaveArticleSuccess() {
        // 模拟数据
        com.wanghui.shiyue.article.entity.po.ArticlePO articlePO = new com.wanghui.shiyue.article.entity.po.ArticlePO();
        articlePO.setArticleId(1L);

        // 模拟方法调用
        when(articleConvert.dtoToPo(articleDTO)).thenReturn(articlePO);
        when(articleMapper.insert(articlePO)).thenReturn(1);

        // 执行测试
        Boolean result = articleService.save(articleDTO);

        // 验证结果
        assertTrue(result);
        verify(articleConvert, times(1)).dtoToPo(articleDTO);
        verify(articleMapper, times(1)).insert(articlePO);
        verify(articleTagService, never()).deleteByArticleId(anyLong());
        verify(articleTagService, never()).saveArticleTags(anyLong(), anyList());
    }

    /**
     * 测试保存文章时缺少标题
     */
    @Test
    void testSaveArticleWithoutTitle() {
        // 设置测试数据
        articleDTO.setTitle(null);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            articleService.save(articleDTO);
        });

        assertEquals("文章标题不能为空", exception.getMessage());
    }

    /**
     * 测试保存文章时缺少内容
     */
    @Test
    void testSaveArticleWithoutContent() {
        // 设置测试数据
        articleDTO.setContent(null);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            articleService.save(articleDTO);
        });

        assertEquals("文章内容不能为空", exception.getMessage());
    }

    /**
     * 测试保存文章时缺少分类
     */
    @Test
    void testSaveArticleWithoutClassId() {
        // 设置测试数据
        articleDTO.setClassId(null);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            articleService.save(articleDTO);
        });

        assertEquals("文章分类不能为空", exception.getMessage());
    }

    /**
     * 测试更新文章
     */
    @Test
    void testUpdateArticleSuccess() {
        // 设置测试数据
        articleDTO.setArticleId(1L);

        // 模拟数据
        com.wanghui.shiyue.article.entity.po.ArticlePO articlePO = new com.wanghui.shiyue.article.entity.po.ArticlePO();
        articlePO.setArticleId(1L);

        // 模拟方法调用
        when(articleConvert.dtoToPo(articleDTO)).thenReturn(articlePO);
        when(articleMapper.updateById(articlePO)).thenReturn(1);
        when(articleTagService.deleteByArticleId(1L)).thenReturn(true);

        // 执行测试
        Boolean result = articleService.save(articleDTO);

        // 验证结果
        assertTrue(result);
        verify(articleConvert, times(1)).dtoToPo(articleDTO);
        verify(articleMapper, times(1)).updateById(articlePO);
        verify(articleTagService, times(1)).deleteByArticleId(1L);
        verify(articleTagService, never()).saveArticleTags(anyLong(), anyList());
    }

    /**
     * 测试保存文章时带标签
     */
    @Test
    void testSaveArticleWithTags() {
        // 设置测试数据
        List<TagDTO> tagList = new ArrayList<>();
        TagDTO tagDTO1 = new TagDTO();
        tagDTO1.setTagId(1L);
        tagList.add(tagDTO1);
        articleDTO.setTagList(tagList);

        // 模拟数据
        com.wanghui.shiyue.article.entity.po.ArticlePO articlePO = new com.wanghui.shiyue.article.entity.po.ArticlePO();
        articlePO.setArticleId(1L);

        // 模拟方法调用
        when(articleConvert.dtoToPo(articleDTO)).thenReturn(articlePO);
        when(articleMapper.insert(articlePO)).thenReturn(1);
        when(articleTagService.saveArticleTags(1L, List.of(1L))).thenReturn(true);

        // 执行测试
        Boolean result = articleService.save(articleDTO);

        // 验证结果
        assertTrue(result);
        verify(articleConvert, times(1)).dtoToPo(articleDTO);
        verify(articleMapper, times(1)).insert(articlePO);
        verify(articleTagService, times(1)).saveArticleTags(1L, List.of(1L));
    }

    /**
     * 测试保存文章时标签为空
     */
    @Test
    void testSaveArticleWithEmptyTags() {
        // 设置测试数据
        articleDTO.setTagList(new ArrayList<>());

        // 模拟数据
        com.wanghui.shiyue.article.entity.po.ArticlePO articlePO = new com.wanghui.shiyue.article.entity.po.ArticlePO();
        articlePO.setArticleId(1L);

        // 模拟方法调用
        when(articleConvert.dtoToPo(articleDTO)).thenReturn(articlePO);
        when(articleMapper.insert(articlePO)).thenReturn(1);

        // 执行测试
        Boolean result = articleService.save(articleDTO);

        // 验证结果
        assertTrue(result);
        verify(articleConvert, times(1)).dtoToPo(articleDTO);
        verify(articleMapper, times(1)).insert(articlePO);
        verify(articleTagService, never()).saveArticleTags(anyLong(), anyList());
    }

    /**
     * 测试保存文章时标签为null
     */
    @Test
    void testSaveArticleWithNullTags() {
        // 设置测试数据
        articleDTO.setTagList(null);

        // 模拟数据
        com.wanghui.shiyue.article.entity.po.ArticlePO articlePO = new com.wanghui.shiyue.article.entity.po.ArticlePO();
        articlePO.setArticleId(1L);

        // 模拟方法调用
        when(articleConvert.dtoToPo(articleDTO)).thenReturn(articlePO);
        when(articleMapper.insert(articlePO)).thenReturn(1);

        // 执行测试
        Boolean result = articleService.save(articleDTO);

        // 验证结果
        assertTrue(result);
        verify(articleConvert, times(1)).dtoToPo(articleDTO);
        verify(articleMapper, times(1)).insert(articlePO);
        verify(articleTagService, never()).saveArticleTags(anyLong(), anyList());
    }

    /**
     * 测试保存文章时带目录
     */
    @Test
    void testSaveArticleWithCatalog() {
        // 设置测试数据
        articleDTO.setCatalogId(1L);

        // 模拟数据
        com.wanghui.shiyue.article.entity.po.ArticlePO articlePO = new com.wanghui.shiyue.article.entity.po.ArticlePO();
        articlePO.setArticleId(1L);

        // 模拟方法调用
        when(articleConvert.dtoToPo(articleDTO)).thenReturn(articlePO);
        when(articleMapper.insert(articlePO)).thenReturn(1);
        when(articleCatalogService.saveArticleCatalogs(1L, java.util.List.of(1L))).thenReturn(true);

        // 执行测试
        Boolean result = articleService.save(articleDTO);

        // 验证结果
        assertTrue(result);
        verify(articleConvert, times(1)).dtoToPo(articleDTO);
        verify(articleMapper, times(1)).insert(articlePO);
        verify(articleCatalogService, times(1)).saveArticleCatalogs(1L, java.util.List.of(1L));
    }

    /**
     * 测试保存文章时带多目录
     */
    @Test
    void testSaveArticleWithMultipleCatalogs() {
        // 设置测试数据
        java.util.List<com.wanghui.shiyue.article.entity.dto.CatalogDTO> catalogList = new java.util.ArrayList<>();
        com.wanghui.shiyue.article.entity.dto.CatalogDTO catalogDTO1 = new com.wanghui.shiyue.article.entity.dto.CatalogDTO();
        catalogDTO1.setCatalogId(1L);
        com.wanghui.shiyue.article.entity.dto.CatalogDTO catalogDTO2 = new com.wanghui.shiyue.article.entity.dto.CatalogDTO();
        catalogDTO2.setCatalogId(2L);
        catalogList.add(catalogDTO1);
        catalogList.add(catalogDTO2);
        articleDTO.setCatalogList(catalogList);

        // 模拟数据
        com.wanghui.shiyue.article.entity.po.ArticlePO articlePO = new com.wanghui.shiyue.article.entity.po.ArticlePO();
        articlePO.setArticleId(1L);

        // 模拟方法调用
        when(articleConvert.dtoToPo(articleDTO)).thenReturn(articlePO);
        when(articleMapper.insert(articlePO)).thenReturn(1);
        when(articleCatalogService.saveArticleCatalogs(1L, java.util.List.of(1L, 2L))).thenReturn(true);

        // 执行测试
        Boolean result = articleService.save(articleDTO);

        // 验证结果
        assertTrue(result);
        verify(articleConvert, times(1)).dtoToPo(articleDTO);
        verify(articleMapper, times(1)).insert(articlePO);
        verify(articleCatalogService, times(1)).saveArticleCatalogs(1L, java.util.List.of(1L, 2L));
    }

    /**
     * 测试更新文章时删除旧的目录关联
     */
    @Test
    void testUpdateArticleWithCatalog() {
        // 设置测试数据
        articleDTO.setArticleId(1L);
        articleDTO.setCatalogId(1L);

        // 模拟数据
        com.wanghui.shiyue.article.entity.po.ArticlePO articlePO = new com.wanghui.shiyue.article.entity.po.ArticlePO();
        articlePO.setArticleId(1L);

        // 模拟方法调用
        when(articleConvert.dtoToPo(articleDTO)).thenReturn(articlePO);
        when(articleMapper.updateById(articlePO)).thenReturn(1);
        when(articleTagService.deleteByArticleId(1L)).thenReturn(true);
        when(articleCatalogService.deleteByArticleId(1L)).thenReturn(true);
        when(articleCatalogService.saveArticleCatalogs(1L, java.util.List.of(1L))).thenReturn(true);

        // 执行测试
        Boolean result = articleService.save(articleDTO);

        // 验证结果
        assertTrue(result);
        verify(articleConvert, times(1)).dtoToPo(articleDTO);
        verify(articleMapper, times(1)).updateById(articlePO);
        verify(articleTagService, times(1)).deleteByArticleId(1L);
        verify(articleCatalogService, times(1)).deleteByArticleId(1L);
        verify(articleCatalogService, times(1)).saveArticleCatalogs(1L, java.util.List.of(1L));
    }
}
