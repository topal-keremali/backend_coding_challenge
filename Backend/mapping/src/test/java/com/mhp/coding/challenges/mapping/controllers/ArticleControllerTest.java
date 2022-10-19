package com.mhp.coding.challenges.mapping.controllers;

import com.mhp.coding.challenges.mapping.models.db.Article;
import com.mhp.coding.challenges.mapping.models.db.blocks.ArticleBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.TextBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.VideoBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.VideoBlockType;
import com.mhp.coding.challenges.mapping.repositories.ArticleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {

    private final Long ID = 1L;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ArticleRepository articleRepository;
    private List<Article> articleList;
    private Article article;
    private Article article2;

    @Before
    public void setUp() {
        article = createDummyArticle(1L);
        article2 = createDummyArticle(2L);
        articleList = new ArrayList<>();
        articleList.add(article);
        articleList.add(article2);
    }

    @Test
    public void test_controller_returning_mapped_list() throws Exception {
        when(articleRepository.all()).thenReturn(articleList);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/article"))
         .andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(articleList.size()))
         .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(article.getId()))
         .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(article.getAuthor()))
         .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(article.getDescription()))
         .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(article.getTitle()))
         .andExpect(MockMvcResultMatchers.jsonPath("$[0].blocks.length()").value(article.getBlocks().size()))
         .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(article2.getId()))
         .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value(article2.getAuthor()))
         .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(article2.getDescription()))
         .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(article2.getTitle()))
         .andExpect(MockMvcResultMatchers.jsonPath("$[1].blocks.length()").value(article2.getBlocks().size()));
    }

    @Test
    public void test_controller_throwing_404() throws Exception {
        when(articleRepository.findBy(ID)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/article/{id}", ID))
         .andExpect(MockMvcResultMatchers.status().isNotFound())
         .andExpect(result -> assertEquals("Article with id: " + ID + " not found.", result.getResponse().getContentAsString()));
    }

    @Test
    public void test_controller_returning_article() throws Exception {
        when(articleRepository.findBy(ID)).thenReturn(article);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/article/{id}", article.getId()))
         .andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(article.getId()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(article.getAuthor()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(article.getDescription()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(article.getTitle()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.blocks.length()").value(article.getBlocks().size()));
    }

    private Article createDummyArticle(Long id) {
        final Article result = new Article();
        result.setId(id);
        result.setAuthor("Max Mustermann");
        result.setDescription("Article Description " + id);
        result.setTitle("Article Nr.: " + id);
        result.setLastModifiedBy("Hans Müller");
        result.setLastModified(new Date());
        result.setBlocks(createBlocks(id));
        return result;
    }

    private Set<ArticleBlock> createBlocks(Long articleId) {
        final Set<ArticleBlock> result = new HashSet<>();

        final TextBlock textBlock = new TextBlock();
        textBlock.setText("Some Text for " + articleId);
        textBlock.setSortIndex(0);
        result.add(textBlock);

        final TextBlock secondTextBlock = new TextBlock();
        secondTextBlock.setText("Second Text for " + articleId);
        secondTextBlock.setSortIndex(1);
        result.add(secondTextBlock);

        final TextBlock thirdTextBlock = new TextBlock();
        thirdTextBlock.setText("Third Text for " + articleId);
        thirdTextBlock.setSortIndex(2);
        result.add(thirdTextBlock);

        final VideoBlock videoBlock = new VideoBlock();
        videoBlock.setType(VideoBlockType.YOUTUBE);
        videoBlock.setUrl("https://youtu.be/myvideo");
        videoBlock.setSortIndex(3);

        result.add(videoBlock);

        return result;
    }
}
