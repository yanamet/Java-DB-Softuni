package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.exam.instagraphlite.models.dto.ImportUserDTO;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    Path path = Path.of
            ("src", "main", "resources", "files", "users.json");
    private final UserRepository userRepository;
    private final PictureService pictureService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final PostRepository postRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PictureService pictureService, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.pictureService = pictureService;
        this.postRepository = postRepository;


        this.gson = new GsonBuilder().create();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importUsers() throws IOException {
        String json = this.readFromFileContent();
        ImportUserDTO[] userDTOS = this.gson.fromJson(json, ImportUserDTO[].class);
        return Arrays.stream(userDTOS)
                .map(this::importUser)
                .collect(Collectors.joining("\n"));
    }

    private String importUser(ImportUserDTO importUserDTO){
        Set<ConstraintViolation<ImportUserDTO>> errors = this.validator.validate(importUserDTO);

        if(!errors.isEmpty()){
            return "Invalid User";
        }

      Optional<User> optUser =  this.userRepository.findByUsername(importUserDTO.getUsername());

        if(optUser.isPresent()){
            return "Invalid User";
        }

       Optional<Picture> optPic = this.pictureService.getPictureByPath(importUserDTO.getProfilePicture());

        if(optPic.isEmpty()){
            return "Invalid User";
        }

        User user = this.modelMapper.map(importUserDTO, User.class);
        user.setProfilePicture(optPic.get());
        this.userRepository.save(user);

        return String.format("Successfully imported User: %s", user.getUsername());

    }

    @Override
    @Transactional
    public String exportUsersWithTheirPosts() {
        List<User> users = this.userRepository.letsTestIt();
        return users.stream().map(User::userToString).collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
}
