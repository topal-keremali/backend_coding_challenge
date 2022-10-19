package com.mhp.coding.challenges.mapping.services;

import com.mhp.coding.challenges.mapping.controllers.ArticleController;
import com.mhp.coding.challenges.mapping.exception.ArticleNotFoundException;
import com.mhp.coding.challenges.mapping.mappers.ArticleMapper;
import com.mhp.coding.challenges.mapping.models.db.Article;
import com.mhp.coding.challenges.mapping.models.db.Image;
import com.mhp.coding.challenges.mapping.models.db.ImageSize;
import com.mhp.coding.challenges.mapping.models.db.blocks.ArticleBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.ImageBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.TextBlock;
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.ArticleBlockDto;
import com.mhp.coding.challenges.mapping.repositories.ArticleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceTest {

    private final static Long ID = 10L;
    private final static Long ArticleID = 1000L;
    @Autowired
    private ArticleMapper articleMapper;
    @MockBean
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleController articleController;
    private Set<ArticleBlock> articleBlockSet;
    private ArticleDto articleDto;

    @Before
    public void setUp() {
        articleBlockSet = new HashSet<>();
        final TextBlock textBlock = new TextBlock();
        textBlock.setText("Some Text for " + ArticleID);
        textBlock.setSortIndex(0);
        articleBlockSet.add(textBlock);

        final ImageBlock imageBlock = new ImageBlock();
        imageBlock.setImage(createImage());
        imageBlock.setSortIndex(4);
        articleBlockSet.add(imageBlock);

        final TextBlock secondTextBlock = new TextBlock();
        secondTextBlock.setText("Second Text for " + ArticleID);
        secondTextBlock.setSortIndex(2);
        articleBlockSet.add(secondTextBlock);
    }

    @Test
    public void contextLoads() {
        assertThat(articleController).isNotNull();
        assertThat(articleMapper).isNotNull();
        assertThat(articleRepository).isNotNull();
        assertThat(articleService).isNotNull();
    }

    @Test(expected = ArticleNotFoundException.class)
    public void test_article_for_id_throwing_exception() {
        when(articleRepository.findBy(ID)).thenReturn(null);

        articleDto = articleService.articleForId(ID);
    }


    @Test
    public void test_article_for_id_returning() {
        Article article = createDummyArticle(ID);
        when(articleRepository.findBy(ID)).thenReturn(article);

        long expected = 10L;
        articleDto = articleService.articleForId(expected);
        long actual = articleDto.getId();
        assertEquals(expected, actual);
        assertEquals(article.getId(), articleDto.getId());
        assertEquals(article.getTitle(), articleDto.getTitle());
        assertEquals(article.getDescription(), articleDto.getDescription());
        assertEquals(article.getAuthor(), articleDto.getAuthor());
    }

    public void test_list_articles() {
        List<Article> articleList = articleList();
        when(articleRepository.all()).thenReturn(articleList);

        long[] expectedIds = new long[5];
        long[] actualIds = new long[5];

        for (int i = 0; i < expectedIds.length; i++) {
            expectedIds[i] = articleList.get(i).getId();
        }

        List<ArticleDto> articleDtoList = articleService.list();

        for (int i = 0; i < actualIds.length; i++) {
            actualIds[i] = articleDtoList.get(i).getId();
        }

        assertArrayEquals(expectedIds, actualIds);
        assertEquals(articleList.size(), articleDtoList.size());
    }

    @Test
    public void test_sorting_block_list() {
        List<ArticleBlockDto> articleBlockDtoList = articleBlockSet.stream()
         .map(b -> articleMapper.mapArticleBlock(b))
         .collect(Collectors.toList());

        List<Integer> expected = Arrays.asList(0, 2, 4);
        articleService.sortBlockList(articleBlockDtoList);
        List<Integer> actual = new ArrayList<>();

        for (ArticleBlockDto articleBlockDto : articleBlockDtoList) {
            actual.add(articleBlockDto.getSortIndex());
        }
        assertEquals(expected, actual);
    }

    private Article createDummyArticle(Long id) {
        final Article result = new Article();
        result.setId(id);
        result.setAuthor("Max Mustermann");
        result.setDescription("Article Description " + id);
        result.setTitle("Article Nr.: " + id);
        result.setLastModifiedBy("Hans Müller");
        result.setLastModified(new Date());
        return result;
    }

    private List<Article> articleList() {
        final List<Article> list = new ArrayList<>();
        list.add(createDummyArticle(1001L));
        list.add(createDummyArticle(2002L));
        list.add(createDummyArticle(3003L));
        list.add(createDummyArticle(4004L));
        list.add(createDummyArticle(5005L));
        return list;
    }

    private Image createImage() {
        final Image result = new Image();
        result.setId((long) 1);
        result.setLastModified(new Date());
        result.setLastModifiedBy("Max Mustermann");
        result.setImageSize(ImageSize.LARGE);
        result.setUrl("https://someurl.com/image/" + (long) 1);
        return result;
    }

}
