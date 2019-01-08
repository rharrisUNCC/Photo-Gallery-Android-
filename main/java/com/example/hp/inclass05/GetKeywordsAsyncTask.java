/*
In-Class Assignment #5
GetKeywordsAsyncTask.java
Ryan Harris
 */
package com.example.hp.inclass05;

import android.media.Image;
import android.os.AsyncTask;

import java.util.LinkedList;

public class GetKeywordsAsyncTask extends AsyncTask<String, Void, LinkedList<String>> {

    IKeyword iKeyword;
    public GetKeywordsAsyncTask (IKeyword iKeyword) { this.iKeyword = iKeyword; }

    @Override
    protected LinkedList<String> doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(LinkedList<String> strings) {
        iKeyword.handleListKeywords(strings);
    }

    public static interface IKeyword {
        public void handleListKeywords(LinkedList<String> keywords);

    }
}
