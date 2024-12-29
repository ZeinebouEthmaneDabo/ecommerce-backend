package mr.iscae.services;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class FileService {

    private static final String BUCKET_NAME = "ecommerce-13ffd.appspot.com";

    public static String uploadFile(MultipartFile multipartFile) throws IOException, StorageException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("MultipartFile is empty");
        }

        Bucket bucket = StorageClient.getInstance().bucket(BUCKET_NAME);
        String blobName = multipartFile.getOriginalFilename();
        Storage storage = bucket.getStorage();
        assert blobName != null;
        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, blobName)
                .setContentType(multipartFile.getContentType())
                .build();
        URL url = storage.signUrl(blobInfo, 500, TimeUnit.DAYS, Storage.SignUrlOption.withV2Signature());
        String signedPath = url.toString();

        bucket.getStorage().create(blobInfo, multipartFile.getBytes());


        return signedPath;
    }
}
