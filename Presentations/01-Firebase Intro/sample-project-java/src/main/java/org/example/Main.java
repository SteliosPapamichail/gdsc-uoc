package org.example;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {

        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream("C:\\Users\\Steli\\Desktop\\GDSC-Firebase-Intro\\gdsc-firebase-intro-firebase-adminsdk-key.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setProjectId("gdsc-firebase-intro")
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();
        ReadAndPrintAllArtworks(db);
    }

    private static void ReadAndPrintAllArtworks(Firestore db) {
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = db.collection("artworks").get();

        // query.get() blocks on response
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                System.out.println("ArtworkId: " + document.getId());
                DocumentReference artistRef = (DocumentReference) document.get("artistId");
                System.out.println("Related artist path: " + artistRef.getPath());
                DocumentReference galleryRef = (DocumentReference) document.get("galleryId");
                System.out.println("Related gallery path: " + galleryRef.getPath());
                System.out.println("Thumbnail url: " + document.getString("thumbnailUrl"));
                System.out.println("Title: " + document.getString("title"));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}