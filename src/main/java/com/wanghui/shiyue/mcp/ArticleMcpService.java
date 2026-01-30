package com.wanghui.shiyue.mcp;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleMcpService {

    @Tool(description = "获取今日文章列表")
    public ResponseResult<List<ArticleDTO>> getTodayArticleList() {
        return null;
    }
}