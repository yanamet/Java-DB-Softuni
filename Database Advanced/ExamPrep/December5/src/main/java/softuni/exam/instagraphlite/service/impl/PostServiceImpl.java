package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.exam.instagraphlite.models.dto.ImportPostDTO;
import softuni.exam.instagraphlite.models.dto.ImportRootPostDTO;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final Unmarshaller unmarshaller;
    Path path = Path.of
            ("src", "main", "resources", "files", "posts.xml");

    private final PostRepository postRepository;
    private final PictureService pictureService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PictureService pictureService,
                           UserService userService) throws JAXBException {
        this.postRepository = postRepository;
        this.pictureService = pictureService;
        this.userService = userService;

        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(ImportRootPostDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {

        ImportRootPostDTO unmarshal = (ImportRootPostDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return unmarshal.getPosts()
                .stream()
                .map(this::importPost)
                .collect(Collectors.joining("\n"));

    }

    private String importPost(ImportPostDTO importPostDTO){
        Set<ConstraintViolation<ImportPostDTO>> errors = this.validator.validate(importPostDTO);

        if(!errors.isEmpty()){
            return "Invalid Post";
        }

        Optional<Picture> optPic = this.pictureService
                .getPictureByPath(importPostDTO.getPicture().getPath());

        if(optPic.isEmpty()){
            return "Invalid Post";
        }

       Optional<User> optUser = this.userService.getUserByUsername(importPostDTO.getUser().getUsername());

        if(optUser.isEmpty()){
            return "Invalid Post";
        }

        Post post = this.modelMapper.map(importPostDTO, Post.class);
        User user = optUser.get();

        post.setUser(user);
        post.setPicture(optPic.get());
        this.postRepository.save(post);

        return String.format("Successfully imported Post, made by %s", post.getUser().getUsername());

    }

}
