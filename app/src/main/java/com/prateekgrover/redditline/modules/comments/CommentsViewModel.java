package com.prateekgrover.redditline.modules.comments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prateekgrover.redditline.models.RedditComment;
import com.prateekgrover.redditline.models.RedditCommentData;
import com.prateekgrover.redditline.models.RedditCommentsResponse;
import com.prateekgrover.redditline.models.RedditPost;
import com.prateekgrover.redditline.repository.network.NetworkManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsViewModel extends ViewModel {

    private RedditPost mRedditPost;
    public MutableLiveData<List<RedditComment>> redditCommentListLiveData;

    public CommentsViewModel(RedditPost redditPost) {
        mRedditPost = redditPost;
        redditCommentListLiveData = new MutableLiveData<>();
        fetchData();
    }

    public void fetchData() {
        Call<RedditCommentsResponse> redditCommentsResponseCall;
        final List<RedditComment> currentRedditComments = redditCommentListLiveData.getValue();

        NetworkManager.getInstance().getRedditAPI().getComments(mRedditPost.getSubredditName(), mRedditPost.getPostId(), 3).enqueue(new Callback<List<RedditCommentsResponse>>() {

            @Override
            public void onResponse(Call<List<RedditCommentsResponse>> call, Response<List<RedditCommentsResponse>> response) {
                List<RedditCommentsResponse> redditCommentsResponseList = response.body();
                if (redditCommentsResponseList != null && !redditCommentsResponseList.isEmpty()) {
                    ArrayList<RedditComment> redditCommentList = new ArrayList<>();
                    for (RedditCommentData redditCommentData : redditCommentsResponseList.get(1).getRedditCommentResponseData().getRedditCommentDataList()) {
                        if (!redditCommentData.getKind().equals("more")) {
                            redditCommentList.add(redditCommentData.getRedditComment());
                        }
                    }

                    List<RedditComment> redditCommentsWithReplies = new ArrayList<>();

                    for (Iterator<RedditComment> it = redditCommentList.iterator(); it.hasNext();) {
                        RedditComment redditComment = it.next();
                        redditCommentsWithReplies.add(redditComment);
                        List<RedditComment> repliesCommentList = getNestedReplies(redditComment);
                        redditCommentsWithReplies.addAll(repliesCommentList);
                    }

                    redditCommentListLiveData.setValue(redditCommentsWithReplies);
                } else {
                    redditCommentListLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<RedditCommentsResponse>> call, Throwable t) {
                System.out.println("Failure");
            }
        });
    }

    private List<RedditComment> getNestedReplies(RedditComment redditComment) {
        List<RedditComment> redditCommentList = new ArrayList<>();
        if (redditComment.getReplies() == null) {
            return redditCommentList;
        }
        for (RedditCommentData redditCommentData : redditComment.getReplies().getRedditCommentResponseData().getRedditCommentDataList()) {
            if (!redditCommentData.getKind().equals("more")) {
                RedditComment newRedditComment = redditCommentData.getRedditComment();
                newRedditComment.setLevel(redditComment.getLevel() + 1);
                redditCommentList.add(newRedditComment);
                List<RedditComment> repliesRedditCommentList = getNestedReplies(newRedditComment);
                redditCommentList.addAll(repliesRedditCommentList);
            }
        }
        return redditCommentList;
    }
}
