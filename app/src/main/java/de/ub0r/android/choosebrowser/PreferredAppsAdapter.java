package de.ub0r.android.choosebrowser;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ub0r.android.logg0r.Log;

public class PreferredAppsAdapter extends RecyclerView.Adapter<PreferredAppsAdapter.ViewHolder> {

    private static final String TAG = "PreferredAppsAdapter";

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView keyView;
        final TextView activityNameView;
        final ImageView iconView;
        final View deleteActionView;

        ViewHolder(final View itemView) {
            super(itemView);
            keyView = itemView.findViewById(R.id.key);
            activityNameView = itemView.findViewById(R.id.activity_name);
            iconView = itemView.findViewById(R.id.activity_icon);
            deleteActionView = itemView.findViewById(R.id.action_delete);
        }

    }

    static class ContentHolder {
        private final String mKey;
        private final ComponentName mComponent;
        private CharSequence mLabel;
        private Drawable mIcon;

        ContentHolder(final String key, final ComponentName component) {
            mKey = key;
            mComponent = component;
        }

        CharSequence getLabel(final PackageManager pm) {
            if (mLabel == null) {
                try {
                    mLabel = pm.getActivityInfo(mComponent, 0).loadLabel(pm);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "error finding activity ", mComponent.flattenToShortString(), e);
                    mLabel = mComponent.flattenToShortString();
                }
            }
            return mLabel;
        }

        Drawable getIcon(final PackageManager pm) {
            if (mIcon == null) {
                try {
                    mIcon = pm.getActivityInfo(mComponent, 0).loadIcon(pm);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "error finding activity ", mComponent.flattenToShortString(), e);
                }
            }
            return mIcon;
        }

        String getKey() {
            return mKey;
        }

        ComponentName getComponent() {
            return mComponent;
        }
    }

    private final LayoutInflater mInflater;
    private final PackageManager mPackageManager;
    private PreferenceStore mStore;
    private final List<ContentHolder> mItems;

    PreferredAppsAdapter(final Context context, final PreferenceStore store) {
        mInflater = LayoutInflater.from(context);
        mPackageManager = context.getPackageManager();
        mStore = store;
        mItems = new ArrayList<>();
        final ArrayList<String> keys = new ArrayList<>(mStore.list());
        Collections.sort(keys);
        for (final String key : keys) {
            mItems.add(new ContentHolder(key, mStore.get(key)));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View itemView = mInflater.inflate(R.layout.item_preferred_apps,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ContentHolder content = mItems.get(position);
        holder.keyView.setText(content.getKey());
        holder.activityNameView.setText(content.getLabel(mPackageManager));
        holder.iconView.setImageDrawable(content.getIcon(mPackageManager));
        holder.deleteActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                deleteItem(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void deleteItem(final int position) {
        final ContentHolder content = mItems.get(position);
        mStore.remove(content.getKey());
        mItems.remove(position);
        notifyItemRemoved(position);
    }
}
