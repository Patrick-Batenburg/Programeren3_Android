package com.patrick.maaltijdapp.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.patrick.maaltijdapp.R;
import com.patrick.maaltijdapp.model.domain.Meal;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 *  Provide views for an AdapterView, Returns a view for each object in a collection of data objects you provide, and can be used with list-based user interface widgets such as ListView or Spinner.
 */
public class MealAdapter extends ArrayAdapter<Meal>
{
    private static final String TAG = MealAdapter.class.getSimpleName();
    private ArrayList<Meal> meals;
    private Context context;
    private File filesDir;

    /**
     * Initializes a new instance of the MealAdapter class from the specified instances of the Context and ArrayList<Meal> classes.
     *
     * @param context The Context object associated with the current state of the application for the current request.
     * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
     * @param meals The objects to represent in the ListView. This value must never be null.
     */
    public MealAdapter(Context context, int resource, ArrayList<Meal> meals)
    {
        super(context, resource, meals);
        this.meals = meals;
        this.context = context;
        this.filesDir = context.getFilesDir();
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view.
     * @param parent The parent that this view will eventually be attached to. This value must never be null.
     * @return A View corresponding to the data at the specified position. This value will never be null.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Log.i(TAG, "getView " + position);
        ViewHolder viewHolder;

        // Create new of gebruik een al bestaande (recycled by Android)
        // The old view to reuse, if possible. Note: You should check
        // that this view is non-null and of an appropriate type before using. If
        // it is not possible to convert this view to display the correct data,
        // this method can create a new view.
        if (convertView != null)
        {
            // Als de view wel bestaat gebruiken we die.
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_view_meal_row, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        Meal meal = getItem(position);

        if (meal != null)
        {
            viewHolder.mealTitleTextView.setText(meal.getDish());
            viewHolder.mealDateTimeTextView.setText(String.format("%s %s", context.getText(R.string.time_icon), meal.getDateTimeToString()));
            viewHolder.mealChefNameTextView.setText(String.format("%s %s", context.getText(R.string.chef_icon), meal.getChef().getFullName()));
            //viewHolder.mealImageView.setImageBitmap(ImageUtility.getImage(meal.getImage()));
            File file = new File(filesDir, "mealImages_" + meal.getID());
            Picasso.with(context).load(file).placeholder(R.drawable.no_image).error(R.drawable.no_image).centerCrop().fit().into(viewHolder.mealImageView);
        }

        return convertView;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position. This value may be null.
     */
    @Override
    public Meal getItem(int position)
    {
        return meals.get(position);
    }


    /**
     * Provides an item view and metadata about its place within the RecyclerView.
     */
    // Holds all data to the view. Wordt evt. gerecycled door Android
    public static class ViewHolder
    {
        @BindView(R.id.meal_image)
        ImageView mealImageView;

        @BindView(R.id.meal_title)
        TextView mealTitleTextView;

        @BindView(R.id.meal_chef_name)
        TextView mealChefNameTextView;

        @BindView(R.id.meal_date_time)
        TextView mealDateTimeTextView;

        /**
         * Binds all the controls to the view.
         *
         * @param view The view to bind to.
         */
        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
