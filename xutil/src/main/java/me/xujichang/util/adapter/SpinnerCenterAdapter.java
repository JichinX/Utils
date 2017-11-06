package me.xujichang.util.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Spinner 居中
 *
 * @author xjc
 *         Created by xjc on 2017/3/17.
 */

public class SpinnerCenterAdapter<T> extends ArrayAdapter<T> {
    private int textRes;

    public SpinnerCenterAdapter(@NonNull Context context, @LayoutRes int resource) {
        this(context, resource, 0);
    }

    public SpinnerCenterAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        this(context, resource, textViewResourceId, new ArrayList<T>());
    }

    public SpinnerCenterAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull T[] objects) {
        this(context, resource, 0, objects);
    }

    public SpinnerCenterAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull T[] objects) {
        this(context, resource, textViewResourceId, Arrays.asList(objects));
    }

    public SpinnerCenterAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> objects) {
        this(context, resource, 0, objects);
    }

    public SpinnerCenterAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public SpinnerCenterAdapter(int res, @NonNull Context context) {
        this(context, android.R.layout.simple_spinner_item, (List<T>) Arrays.asList(context.getResources().getStringArray(res)));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return setCentered(super.getView(position, convertView, parent));
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return setCentered(super.getDropDownView(position, convertView, parent));
    }

    private View setCentered(View view) {
        if (textRes == 0) {
            textRes = android.R.id.text1;
        }
        TextView textView = (TextView) view.findViewById(textRes);
        textView.setGravity(Gravity.CENTER);
        return view;
    }
}
