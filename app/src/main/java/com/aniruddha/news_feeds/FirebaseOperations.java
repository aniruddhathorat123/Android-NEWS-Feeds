package com.aniruddha.news_feeds;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class responsible to perform all operations related to Firestore DB.
 */
public class FirebaseOperations {
    private static final String TAG = "FirebaseOperations";
    private static final String RSS_COLLECTION_NAME = "news_rss";
    private static final String DELETE_RSS_COLLECTION_NAME = "deleted_news_rss";
    private static final String RSS_NAME = "name";
    private static final String RSS_IMAGE_LINK = "image_link";
    private static final String RSS_SOURCE_LINK = "source_link";
    private static final String IS_RSS_VISIBLE = "visible";

    private static FirebaseOperations firebaseOperations;
    private FirebaseFirestore db = null;
    private FirestoreOperationCallback callback;

    // class constructor.
    // Singleton instance of constructor and Firestore DB.
    public static FirebaseOperations getInstance(FirestoreOperationCallback callback) {
        if (firebaseOperations == null) {
            firebaseOperations = new FirebaseOperations();
            // initialise the firestore DB.
            firebaseOperations.initFirestoreDB();
            firebaseOperations.callback = callback;
        }
        return firebaseOperations;
    }

    /**
     * Fetch all available RSS feeds with their details from firestore DB.
     * Execute {@link FirestoreOperationCallback#onRssSyncCompletion} method once all RSS feeds
     * details are fetched from Firestore.
     */
    public void getAllRssDetails() {
        List<RssEntity> rssEntities = new ArrayList<>();
        CollectionReference colRef = db.collection(RSS_COLLECTION_NAME);

        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.DEFAULT;

        colRef.get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "#getAllRssDetails success.");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        RssEntity rssEntity = new RssEntity();
                        rssEntity.rssId = Integer.parseInt(document.getId());
                        rssEntity.rssName = String.valueOf(document.get(RSS_NAME));
                        rssEntity.rssImage = String.valueOf(document.get(RSS_IMAGE_LINK));
                        rssEntity.rssSource = String.valueOf(document.get(RSS_SOURCE_LINK));
                        rssEntity.isRssAvail =
                                Boolean.TRUE.equals(document.getBoolean(IS_RSS_VISIBLE));
                        rssEntities.add(rssEntity);
                    }
                    callback.onRssSyncCompletion(rssEntities);
                } else {
                    Log.d(TAG, "#getAllRssDetails failed: ", task.getException());
                }
            }
        });
    }

    /**
     * Fetch all RSS feeds which app no longer supported and they should be remove from local app's
     * Database.
     * Execute {@link FirestoreOperationCallback#onDeletedRssSyncCompletion} method once all removed
     * RSS feeds details are fetched from Firestore.
     */
    public void getAllDeletedRssDetails() {
        List<RssEntity> rssEntities = new ArrayList<>();
        CollectionReference colRef = db.collection(DELETE_RSS_COLLECTION_NAME);

        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.DEFAULT;
        colRef.get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "#getAllDeletedRssDetails success.");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        RssEntity rssEntity = new RssEntity();
                        rssEntity.rssId = Integer.parseInt(document.getId());
                        rssEntity.rssName = String.valueOf(document.get(RSS_NAME));
                        rssEntities.add(rssEntity);
                    }
                } else {
                    Log.d(TAG, "#getAllDeletedRssDetails failed: ", task.getException());
                }
                callback.onDeletedRssSyncCompletion(rssEntities);
            }
        });
    }

    private void initFirestoreDB() {
        db = FirebaseFirestore.getInstance();
    }
}

/**
 * Callback listener used by classed to listen the status of firestore DB operation.
 * Classes which are using the methods of {@link FirebaseOperations} must implements this interface
 * inorder to get the update of Firestore DB operations.
 */
interface FirestoreOperationCallback {
    void onRssSyncCompletion(List<RssEntity> rssEntities);
    void onDeletedRssSyncCompletion(List<RssEntity> rssEntities);
}
