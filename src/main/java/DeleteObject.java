import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class DeleteObject {
    public static void deleteObject(String objectName, GoogleCredentials credentials) {
        String projectId="mercurial-shape-342406";
        String bucketName="project-gv-folder";

        Storage storage=StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
        storage.delete(bucketName,objectName);
        System.out.println("Object " + objectName + " was deleted from " + bucketName);
    }
}
