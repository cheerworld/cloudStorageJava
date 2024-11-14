package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.controller.HomeController;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class CredentialService {

    @Autowired
    private CredentialMapper credentialMapper;

    @Autowired
    private EncryptionService encryptionService;
    private final Logger logger = LoggerFactory.getLogger(CredentialService.class);

    public List<Credential> getAllCredentialsByUserId(Integer userId) {
        return credentialMapper.getCredentialsByUser(userId);
    }

    public void saveCredential(Credential credential, Integer userId) {
        byte[] keyBytes = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);
        String encodedKey = Base64.getEncoder().encodeToString(keyBytes);

        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        logger.info("encryptedPassword: {}", encryptedPassword);
        credential.setUserId(userId);
        credential.setSalt(encodedKey);
        credential.setPassword(encryptedPassword);

        logger.info("credential: {}", credential.toString());
        if (credential.getCredentialId() == null) {
            credentialMapper.insert(credential);
        } else {
            credentialMapper.update(credential);
        }
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }

    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public String decryptPassword(Credential credential) {
        return encryptionService.decryptValue(credential.getPassword(), credential.getSalt());
    }
}
