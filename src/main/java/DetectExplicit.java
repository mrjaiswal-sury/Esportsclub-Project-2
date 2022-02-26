import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.common.collect.Lists;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DetectExplicit {

    public static GoogleCredentials authExplicit(String jsonPath) throws IOException{
        return GoogleCredentials.fromStream(new FileInputStream(jsonPath)).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
    }

    public static void detectSafeSearch(String filePath, String objectName)throws IOException{
        List<AnnotateImageRequest> requests=new ArrayList<>();

        ImageSource imgSource=ImageSource.newBuilder().setGcsImageUri(filePath).build();

        Image image=Image.newBuilder().setSource(imgSource).build();
        Feature feat=Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
        AnnotateImageRequest request=AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(image).build();
        requests.add(request);

        try(ImageAnnotatorClient client=ImageAnnotatorClient.create()){
            BatchAnnotateImagesResponse response=client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses=response.getResponsesList();

            for(AnnotateImageResponse res: responses){
                if(res.hasError()){
                    System.out.format("Error: %s%n",res.getError().getMessage());
                    return;
                }

                SafeSearchAnnotation annotation=res.getSafeSearchAnnotation();
//                System.out.format("adult: %s%nmedical: %s%nspoofed: %s%nviolence: %s%nracy: %s%n",annotation.getAdult(),annotation.getMedical(),annotation.getSpoof(),annotation.getViolence(),annotation.getRacy());
                if(annotation.getAdult().equals(Likelihood.LIKELY) || annotation.getAdult().equals(Likelihood.VERY_LIKELY) ||
                        annotation.getMedical().equals(Likelihood.LIKELY) || annotation.getMedical().equals(Likelihood.VERY_LIKELY) ||
                        annotation.getSpoof().equals(Likelihood.LIKELY) || annotation.getSpoof().equals(Likelihood.VERY_LIKELY) ||
                        annotation.getViolence().equals(Likelihood.LIKELY) || annotation.getViolence().equals(Likelihood.VERY_LIKELY) ||
                        annotation.getRacy().equals(Likelihood.LIKELY) || annotation.getRacy().equals(Likelihood.VERY_LIKELY)){
                    System.out.println("Explicit Content Detected");
                    DeleteObject.deleteObject(objectName,authExplicit("E:\\Users\\Suryaansh Jaiswal\\Desktop\\key\\mercurial-shape-342406-57be7ff47199.json"));
                }

            }
        }
    }
}
