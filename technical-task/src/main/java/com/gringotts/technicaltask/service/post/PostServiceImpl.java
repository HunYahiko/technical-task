package com.gringotts.technicaltask.service.post;

import com.gringotts.technicaltask.domain.Post;
import com.gringotts.technicaltask.exception.EntityNotFoundException;
import com.gringotts.technicaltask.repository.PostRepository;
import com.gringotts.technicaltask.service.post.validator.AddPostValidator;
import com.gringotts.technicaltask.utils.Mapper;
import com.gringotts.technicaltask.web.post.CreatePostData;
import com.gringotts.technicaltask.web.post.PostDto;
import com.gringotts.technicaltask.web.post.UpdatePostData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final Mapper<Post, PostDto> mapper;
    private final List<AddPostValidator> validators;

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto> getAll(Pageable pageable) {
        return postRepository.findAll(pageable)
                             .map(mapper::map);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getById(Long id) {
        final Post post = postRepository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException(Post.class, id));
        return mapper.map(post);
    }

    @Override
    @Transactional
    public void create(CreatePostData data) {
        validators.forEach(validator -> validator.validate(data));
        postRepository.save(new Post(data.getTitle(),
                                     data.getContent(),
                                     data.getDescription()));
    }

    @Override
    @Transactional
    public void update(UpdatePostData data) {
        final Post post = data.getId()
                              .flatMap(postRepository::findById)
                              .orElseThrow(() -> new EntityNotFoundException(Post.class));
        data.getTitle().ifPresent(post::setTitle);
        data.getDescription().ifPresent(post::setDescription);
        data.getContent().ifPresent(post::setContent);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}
