package com.mhp.coding.challenges.mapping.mappers;

import com.mhp.coding.challenges.mapping.models.db.Article;
import com.mhp.coding.challenges.mapping.models.db.blocks.ArticleBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.TextBlock;
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ArticleMapperTest {

    ArticleMapper articleMapper = new ArticleMapper();

    @Test
    public void test_correct_mapping_article_to_dto() {
        Article article = new Article();
        article.setId(123123123L);
        article.setTitle("TestTitel");
        article.setAuthor("TestAutor");
        article.setDescription("TestBeschreibung");
        article.setBlocks(getArticleBlocks());
        ArticleDto articleDto = articleMapper.map(article);

        assertEquals(article.getAuthor(), articleDto.getAuthor());
        assertEquals(article.getTitle(), articleDto.getTitle());
        assertEquals(article.getId(), articleDto.getId());
        assertEquals(article.getDescription(), articleDto.getDescription());
        assertEquals(article.getAuthor(), articleDto.getAuthor());
    }

    private Set<ArticleBlock> getArticleBlocks() {
        Set<ArticleBlock> result = new HashSet<>();

        TextBlock textBlock = new TextBlock();
        textBlock.setText("Some Text");
        textBlock.setSortIndex(0);
        result.add(textBlock);

        TextBlock textBlock1 = new TextBlock();
        textBlock.setText("Some Texts");
        textBlock.setSortIndex(1);
        result.add(textBlock1);
        return result;
    }

    @Test
    public void test_correct_mapping_article_to_dto_missing_field() {
        Article article = new Article();
        article.setId(123123123L);
        article.setTitle("TestTitel");
        article.setAuthor("TestAutor");
//        article.setDescription("TestBeschreibung");
        ArticleDto articleDto = articleMapper.map(article);

        assertEquals(article.getAuthor(), articleDto.getAuthor());
        assertEquals(article.getTitle(), articleDto.getTitle());
        assertEquals(article.getId(), articleDto.getId());
        assertEquals(article.getDescription(), articleDto.getDescription());
        assertEquals(article.getAuthor(), articleDto.getAuthor());
    }


}
