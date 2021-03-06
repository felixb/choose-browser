package de.ub0r.android.choosebrowser;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChooserAdapter extends RecyclerView.Adapter<ChooserAdapter.ViewHolder> {

    interface OnItemClickListener {
        void onItemClick(final ComponentName component);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final View containerView;
        final TextView activityNameView;
        final ImageView iconView;

        ViewHolder(final View itemView) {
            super(itemView);
            activityNameView = itemView.findViewById(R.id.activity_name);
            iconView = itemView.findViewById(R.id.activity_icon);
            containerView = itemView;
        }

    }

    static class ContentHolder {
        private final ResolveInfo mResolveInfo;
        private CharSequence mLabel;
        private Drawable mIcon;

        ContentHolder(final ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
        }

        CharSequence getLabel(final PackageManager pm) {
            if (mLabel == null) {
                mLabel = mResolveInfo.loadLabel(pm);
            }
            return mLabel;
        }

        Drawable getIcon(final PackageManager pm) {
            if (mIcon == null) {
                mIcon = mResolveInfo.loadIcon(pm);
            }
            return mIcon;
        }

        ComponentName getComponent() {
            return new ComponentName(mResolveInfo.activityInfo.packageName, mResolveInfo.activityInfo.name);
        }
    }

    private final LayoutInflater mInflater;
    private final PackageManager mPackageManager;
    private final OnItemClickListener mListener;
    private final List<ContentHolder> mItems;

    ChooserAdapter(final Context context, final OnItemClickListener listener, final List<ResolveInfo> items) {
        mInflater = LayoutInflater.from(context);
        mPackageManager = context.getPackageManager();
        mListener = listener;
        mItems = new ArrayList<>();
        final String myPackageName = context.getPackageName();
        for (ResolveInfo resolveInfo : items) {
            if (!myPackageName.equals(resolveInfo.activityInfo.packageName)) {
                mItems.add(new ContentHolder(resolveInfo));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View itemView = mInflater.inflate(R.layout.item_chooser,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ContentHolder content = mItems.get(position);
        holder.activityNameView.setText(content.getLabel(mPackageManager));
        holder.iconView.setImageDrawable(content.getIcon(mPackageManager));
        if (mListener != null) {
            holder.containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    mListener.onItemClick(content.getComponent());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
