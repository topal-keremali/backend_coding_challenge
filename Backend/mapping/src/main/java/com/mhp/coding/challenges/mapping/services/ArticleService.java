package com.mhp.coding.challenges.mapping.services;

import com.mhp.coding.challenges.mapping.exception.ArticleNotFoundException;
import com.mhp.coding.challenges.mapping.mappers.ArticleMapper;
import com.mhp.coding.challenges.mapping.models.db.Article;
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.ArticleBlockDto;
import com.mhp.coding.challenges.mapping.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository repository;

    private final ArticleMapper mapper;

    @Autowired
    public ArticleService(ArticleRepository repository, ArticleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ArticleDto> list() {
        final List<Article> articles = repository.all();

        List<ArticleDto> articleDtoList = articles.stream()
         .map(mapper::map)
         .collect(Collectors.toList());
        articleDtoList.forEach(articleDto -> sortBlockList((List<ArticleBlockDto>) articleDto.getBlocks()));

        return articleDtoList;
    }

    public ArticleDto articleForId(Long id) {
        final Article article = repository.findBy(id);

        if (article != null) {
            ArticleDto articleDto = mapper.map(article);
            if (articleDto.getBlocks() != null)
                sortBlockList((List<ArticleBlockDto>) articleDto.getBlocks());
            return articleDto;
        } else throw new ArticleNotFoundException("Article with id: " + id + " not found.");
    }

    public ArticleDto create(ArticleDto articleDto) {
        final Article create = mapper.map(articleDto);
        repository.create(create);
        return mapper.map(create);
    }

    public void sortBlockList(List<ArticleBlockDto> articleBlockDtoList) {
        articleBlockDtoList.sort(Comparator.comparing(ArticleBlockDto::getSortIndex));
    }
}
