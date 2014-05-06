package org.openforis.collect.android.gui.list;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.openforis.collect.R;
import org.openforis.collect.android.gui.util.AndroidVersion;
import org.openforis.collect.android.viewmodel.UiInternalNode;
import org.openforis.collect.android.viewmodel.UiNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Wiell
 */
public class NodeListAdapter extends BaseAdapter {
    private static final int LAYOUT_RESOURCE_ID = R.layout.listview_node;
    private final Context context;
    private final List<UiNode> nodes;

    public NodeListAdapter(Context context, UiInternalNode parentNode) {
        this.context = context;
        this.nodes = new ArrayList<UiNode>(parentNode.getChildren());
    }

    public UiNode getItem(int position) {
        return nodes.get(position);
    }

    public int getCount() {
        return nodes.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NodeHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
            if (AndroidVersion.greaterThan10()) {
                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.activatedBackgroundIndicator, typedValue, true);
                row.setBackgroundResource(typedValue.resourceId);
            }

            holder = new NodeHolder();
            holder.text = (TextView) row.findViewById(R.id.nodeLabel);
            holder.status = (ImageView) row.findViewById(R.id.nodeStatus);

            row.setTag(holder);
        } else {
            holder = (NodeHolder) row.getTag();
        }

        UiNode node = nodes.get(position);
        holder.text.setText(getText(node));
        if (!node.isRelevant())
            holder.text.setTextColor(Color.parseColor("#636363")); // TODO: Don't hard-code colors
        else
            holder.text.setTextColor(Color.parseColor("#FFFFFF"));
        holder.status.setImageResource(iconResource(node));

        return row;
    }

    public String getText(UiNode node) {
        return node.getLabel();
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private int iconResource(UiNode node) {
        switch (node.getStatus()) {
            case OK:
            case EMPTY:
                return 0;
            case VALIDATION_WARNING:
                return R.drawable.yellow_circle;
            case VALIDATION_ERROR:
                return R.drawable.red_circle;
            default:
                throw new IllegalStateException("Unexpected node status: " + node.getStatus());
        }
    }

    public void insert(int position, UiNode node) {
        nodes.add(position, node);
        notifyDataSetChanged();
//        parentNode.addChild(position, node);
//        node.init();

    }

    public void remove(int position) {
        UiNode node = nodes.get(position);
//        node.removeFromParent();
//        node.getParent().unregister(node);
        nodes.remove(position);
        notifyDataSetChanged();
    }

    private static class NodeHolder {
        TextView text;
        ImageView status;
    }
}
