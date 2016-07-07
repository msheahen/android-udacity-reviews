package rocks.athrow.android_udacity_reviews;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Date;

import rocks.athrow.android_udacity_reviews.Data.RealmReview;
import rocks.athrow.android_udacity_reviews.RealmAdapter.RealmRecyclerViewAdapter;

/**
 * ReviewListAdapter
 * Binds the data from the Review Realm Objects to the
 * ReviewListFragmentActivity RecyclerView
 */
public class ReviewListAdapter extends RealmRecyclerViewAdapter<RealmReview> {
    private Context context;

    private class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout viewReviewItem;
        public TextView viewReviewId;
        public TextView viewProjectName;
        public TextView viewCompletedAt;
        public TextView viewUserName;
        public TextView viewResult;
        public TextView viewFilename;


        public ViewHolder(View view) {
            super(view);
            viewReviewItem = (LinearLayout) view.findViewById(R.id.review_item);
            viewProjectName = (TextView) view.findViewById(R.id.review_item_project_name);
            viewCompletedAt = (TextView) view.findViewById(R.id.review_item_completed_at);
            viewUserName = (TextView) view.findViewById(R.id.review_user_name);
            viewUserName = (TextView) view.findViewById(R.id.review_user_name);
            viewResult = (TextView) view.findViewById(R.id.review_result);
            viewReviewId = (TextView) view.findViewById(R.id.review_id);

        }
    }

    public ReviewListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View reviewListRecyclerView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(reviewListRecyclerView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder reviewListRecyclerView = (ViewHolder) viewHolder;
        // Get the review record
        RealmReview reviewRecord = getItem(position);
        //------------------------------------------------------------------------------------------
        // Set the review variables
        //------------------------------------------------------------------------------------------
        final String id = Integer.toString(reviewRecord.getId());
        final String projectName = reviewRecord.getProject_name();
        final Date assignedAt = reviewRecord.getAssigned_at();
        final Date completedAt = reviewRecord.getCompleted_at();
        final String userName = reviewRecord.getUser_name();
        final String result = reviewRecord.getResult();
        final String archiveUrl = reviewRecord.getArchive_url();
        final String studentNotes = reviewRecord.getNotes();
        //------------------------------------------------------------------------------------------
        // Format the dates for the List and Detail Views
        //------------------------------------------------------------------------------------------
        Utilities util = new Utilities();
        final String completedAtListDisplay = util.getDateAsString(completedAt, "MM/dd/yy");
        final String completedAtDetailDisplay = util.getDateAsString(completedAt, "MM/dd/yy h:mm a") ;//"MM/dd/yy h:mm a");
        final String assignedAtDetailDisplay = util.getDateAsString(assignedAt,"MM/dd/yy h:mm a");  //"MM/dd/yy h:mm a");
        //------------------------------------------------------------------------------------------
        // Get the elapsed time between start/end
        //------------------------------------------------------------------------------------------
        // TODO: Store this value in the database?
        final String elapsedTime = util.elapsedTime(assignedAt, completedAt);
        //------------------------------------------------------------------------------------------
        // Get the filename from the archive url
        //------------------------------------------------------------------------------------------
        String[] urlItems = util.stringSplit(archiveUrl,"/");
        int urtlItemsCount = urlItems.length;
        final String fileName = urlItems[urtlItemsCount-1];
        //------------------------------------------------------------------------------------------
        // Set the views
        //------------------------------------------------------------------------------------------
        reviewListRecyclerView.viewReviewId.setText(id);
        reviewListRecyclerView.viewProjectName.setText(projectName);
        reviewListRecyclerView.viewCompletedAt.setText(completedAtListDisplay);
        reviewListRecyclerView.viewUserName.setText(userName);
        reviewListRecyclerView.viewUserName.setText(userName);

        String resultDisplay;
        if (result.equals("passed")) {
            resultDisplay = "P";
            reviewListRecyclerView.viewResult.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_passed) );
        } else if (result.equals("failed")) {
            resultDisplay = "F";
            reviewListRecyclerView.viewResult.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_failed) );
        } else {
            resultDisplay = "CR";
            reviewListRecyclerView.viewResult.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_cant_review) );
            reviewListRecyclerView.viewResult.setTextSize(10);
        }

        reviewListRecyclerView.viewResult.setText(resultDisplay);
        reviewListRecyclerView.viewReviewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewDetailsActivity = new Intent(context, ReviewsDetailActivity.class);
                viewDetailsActivity.putExtra("id", id);
                viewDetailsActivity.putExtra("project_name", projectName);
                viewDetailsActivity.putExtra("assigned_at", assignedAtDetailDisplay);
                viewDetailsActivity.putExtra("completed_at", completedAtDetailDisplay);
                viewDetailsActivity.putExtra("user_name", userName);
                viewDetailsActivity.putExtra("result", result);
                viewDetailsActivity.putExtra("archive_url", archiveUrl);
                viewDetailsActivity.putExtra("filename", fileName);
                viewDetailsActivity.putExtra("elapsed_time", elapsedTime);
                viewDetailsActivity.putExtra("notes", studentNotes);

                context.startActivity(viewDetailsActivity);
            }
        });


    }

    /* The inner RealmBaseAdapter
     * view count is applied here.
     *
     * getRealmAdapter is defined in RealmRecyclerViewAdapter.
     */
    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }
}
