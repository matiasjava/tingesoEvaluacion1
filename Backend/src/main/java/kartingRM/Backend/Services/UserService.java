package kartingRM.Backend.Services;

import kartingRM.Backend.Entities.UserEntity;
import kartingRM.Backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Método para actualizar la categoría de frecuencia del usuario
    // Despues veo si es necesario
    public UserEntity updateCategoryFrequency(Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            int visits = user.getNumberVisits();
            if (visits >= 7) {
                user.setCategory_frecuency("Muy frecuente");
            } else if (visits >= 5) {
                user.setCategory_frecuency("Frecuente");
            } else if (visits >= 2) {
                user.setCategory_frecuency("Regular");
            } else {
                user.setCategory_frecuency("No frecuente");
            }

            return userRepository.save(user);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
    }

    // Método para actualizar el número de visitas y la categoría de frecuencia
    // Despues veo si es necesario
    public UserEntity updateNumberVisits(Long userId, int newVisits) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
    
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setNumberVisits(newVisits);
    
            if (newVisits >= 7) {
                user.setCategory_frecuency("Muy frecuente");
            } else if (newVisits >= 5) {
                user.setCategory_frecuency("Frecuente");
            } else if (newVisits >= 2) {
                user.setCategory_frecuency("Regular");
            } else {
                user.setCategory_frecuency("No frecuente");
            }
    
            return userRepository.save(user);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
    }

    public UserEntity incrementVisitsAndUpdateCategory(Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
    
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
    
            int newVisits = user.getNumberVisits() + 1;
            user.setNumberVisits(newVisits);
    
            if (newVisits >= 7) {
                user.setCategory_frecuency("Muy frecuente");
            } else if (newVisits >= 5) {
                user.setCategory_frecuency("Frecuente");
            } else if (newVisits >= 2) {
                user.setCategory_frecuency("Regular");
            } else {
                user.setCategory_frecuency("No frecuente");
            }
    
            return userRepository.save(user);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(long id) { return userRepository.findById(id).get(); }

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

}
