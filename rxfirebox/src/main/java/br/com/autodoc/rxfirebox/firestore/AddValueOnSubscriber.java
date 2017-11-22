package br.com.autodoc.rxfirebox.firestore;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class AddValueOnSubscriber<T> implements CompletableOnSubscribe {

    private final Map<String, T>  value;
    private final CollectionReference reference;

    public AddValueOnSubscriber(Map<String, T> value, CollectionReference reference) {
        this.value = value;
        this.reference = reference;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception {
        reference.add((Map<String, Object>) value)
                .addOnSuccessListener(new RxCompletionListener(e))
                .addOnFailureListener(new RxCompletionListener(e));

    }

    private static class RxCompletionListener<T> implements OnSuccessListener<QuerySnapshot>,OnFailureListener {

        private final CompletableEmitter subscriber;

        public RxCompletionListener(CompletableEmitter subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onSuccess(@NonNull QuerySnapshot documentSnapshots) {
            subscriber.onComplete();
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            subscriber.onError(e);
        }
    }
}
