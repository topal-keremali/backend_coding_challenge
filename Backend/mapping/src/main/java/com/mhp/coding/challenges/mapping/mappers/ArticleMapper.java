package com.mhp.coding.challenges.mapping.mappers;

import com.mhp.coding.challenges.mapping.models.db.*;
import com.mhp.coding.challenges.mapping.models.db.blocks.*;
import com.mhp.coding.challenges.mapping.models.dto.*;
import com.mhp.coding.challenges.mapping.models.dto.blocks.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public ArticleDto map(Article article) {
        ArticleDto articleDto = modelMapper.map(article,ArticleDto.class);

        if(article.getBlocks()!=null){
            List<ArticleBlockDto> articleBlockDtoList = article.getBlocks().stream().map(this::mapArticleBlock).collect(Collectors.toList());
            articleDto.setBlocks(articleBlockDtoList);
        }

        return articleDto;
    }

    public Article map(ArticleDto articleDto) {
        // Nicht Teil dieser Challenge.
        return new Article();
    }

    public ArticleBlockDto mapArticleBlock(ArticleBlock articleBlock){
        if (articleBlock instanceof TextBlock) {
            return modelMapper.map(articleBlock, TextBlockDto.class);
        } else if (articleBlock instanceof ImageBlock) {
            return modelMapper.map(articleBlock, ImageBlockDto.class);
        } else if (articleBlock instanceof GalleryBlock) {
            return modelMapper.map(articleBlock, GalleryBlockDto.class);
        } else if (articleBlock instanceof VideoBlock) {
            return modelMapper.map(articleBlock, VideoBlockDto.class);
        }
        return null;
    }
}
