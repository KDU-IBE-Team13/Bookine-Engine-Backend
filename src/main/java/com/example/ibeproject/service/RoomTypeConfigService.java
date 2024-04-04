package com.example.ibeproject.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.ibeproject.dto.roomtype.RoomTypeConfigDTO;
import com.example.ibeproject.exceptions.ConfigLoadException;
import com.example.ibeproject.exceptions.ConfigUpdateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class RoomTypeConfigService {
    private final ObjectMapper objectMapper;
    private final String azureBlobConnectionString;
    private final String containerName;

    public RoomTypeConfigService(
            ObjectMapper objectMapper,
            @Value("${config.azure.blob.connection.string}") String azureBlobConnectionString,
            @Value("${config.azure.blob.container.name}") String containerName
    ) {
        this.objectMapper = objectMapper;
        this.azureBlobConnectionString = azureBlobConnectionString;
        this.containerName = containerName;
    }

    /**
     * Writes the updated roomType configuration to Azure Blob Storage.
     * @param updatedConfig Updated roomType configuration.
     * @param blobName Name of the blob where the configuration will be stored.
     * @throws ConfigUpdateException if there is an error while updating the configuration.
     */
    public void writeConfigToAzureBlob(RoomTypeConfigDTO updatedConfig, String blobName) {
        try {
            BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
            BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);

            String jsonString = objectMapper.writeValueAsString(updatedConfig);

            InputStream dataStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));

            blobClient.upload(dataStream, jsonString.length(), true);
        } catch (IOException e) {
            throw new ConfigUpdateException("Failed to write roomType configuration to Azure Blob", e);
        }
    }

    /**
     * Loads the roomType configuration from Azure Blob Storage.
     * @param blobName Name of the blob where the configuration is stored.
     * @return RoomTypeConfigDTO representing the loaded roomType configuration.
     * @throws ConfigLoadException if there is an error while loading the configuration.
     */
    public RoomTypeConfigDTO loadConfigFromAzureBlob(String blobName) {
        try {
            BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder().connectionString(azureBlobConnectionString);
            BlobClient blobClient = clientBuilder.buildClient().getBlobContainerClient(containerName).getBlobClient(blobName);

            byte[] data = blobClient.downloadContent().toBytes();
            return objectMapper.readValue(new ByteArrayInputStream(data), RoomTypeConfigDTO.class);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load roomType configuration from Azure Blob", e);
        }
    }
}
