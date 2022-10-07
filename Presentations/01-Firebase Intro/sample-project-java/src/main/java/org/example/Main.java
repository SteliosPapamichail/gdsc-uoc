package org.example;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {

        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream("C:\\Users\\Steli\\Desktop\\gdsc-firebase-intro-firebase-adminsdk-key.json");
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
        readAndPrintAllArtworks(db);
        System.out.println("---------------------------\n");
        createNewGalleryEntry(db, false);
        System.out.println("---------------------------\n");
        createNewGalleryEntry(db, true);
        System.out.println("---------------------------\n");
        deleteSampleArtistEntry(db);
    }

    private static void deleteSampleArtistEntry(Firestore db) {
        // asynchronously delete a document
        ApiFuture<WriteResult> writeResult = db.collection("artists").document("pls-dont-do-this").delete();
        try {
            System.out.println("Entry deleted successfully!\nUpdate time : " + writeResult.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createNewGalleryEntry(Firestore db, boolean autoGenId) {
        HashMap<String, Object> galleryInfo = new HashMap<>();
        if (!autoGenId) {
            galleryInfo.put("gName", "Dio Horia");
            galleryInfo.put("location", new GeoPoint(10.051, 110.000));
            // Add a new document (asynchronously) in collection "galleries" with id "dio_horia"
            ApiFuture<WriteResult> future = db.collection("galleries").document("dio_horia").set(galleryInfo/*, SetOptions.merge()*/);
            try {
                // future.get() blocks on response
                System.out.println("New entry created successfully!\nUpdate time : " + future.get().getUpdateTime());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else {
            galleryInfo.put("gName", "AutoGen Gallery");
            galleryInfo.put("location", new GeoPoint(0.00, 170.00));
            // Add a new document (asynchronously) in collection "galleries" with auto-generated id
            ApiFuture<DocumentReference> docRef = db.collection("galleries").add(galleryInfo);
            try {
                // future.get() blocks on response
                System.out.println("Added document with ID: " + docRef.get().getId());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void readAndPrintAllArtworks(Firestore db) {
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