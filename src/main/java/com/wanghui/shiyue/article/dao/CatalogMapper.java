package com.wanghui.shiyue.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.shiyue.article.entity.po.CatalogPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 目录数据访问接口
 * @description: 用于目录的数据库操作，继承BaseMapper并扩展自定义方法
 * @fileName: CatalogMapper
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper
public interface CatalogMapper extends BaseMapper<CatalogPO> {

    /**
     * 根据父目录id查询子目录
     * @param parentId 父目录id
     * @return {@link List }<{@link CatalogPO }>
     */
    List<CatalogPO> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 查询所有目录，按层级和路径排序
     * @return {@link List }<{@link CatalogPO }>
     */
    List<CatalogPO> selectAllOrdered();

    /**
     * 根据目录路径查询目录
     * @param path 目录路径
     * @return {@link CatalogPO }
     */
    CatalogPO selectByPath(@Param("path") String path);

    /**
     * 根据目录层级查询目录
     * @param level 目录层级
     * @return {@link List }<{@link CatalogPO }>
     */
    List<CatalogPO> selectByLevel(@Param("level") Integer level);
}
