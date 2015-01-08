package com.cytx.timecard.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cytx.timecard.R;
import com.cytx.timecard.constants.Constants;
import com.cytx.timecard.dto.RecieverDto;
import com.cytx.timecard.utility.FileTools;
import com.cytx.timecard.utility.Utils;

import java.util.List;

/**
 * 创建时间：2014年8月19日 下午8:24:18 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：ReceiverAdapter.java 类说明：
 */
public class HealthCheckAdapter extends BaseAdapter {

	private Context context;
	private List<RecieverDto> receiverList;

	public List<RecieverDto> getReceiverList() {
		return receiverList;
	}

	public void setReceiverList(List<RecieverDto> receiverList) {
		this.receiverList = receiverList;
	}

	public HealthCheckAdapter(Context context, List<RecieverDto> receiverList) {
		this.context = context;
		this.receiverList = receiverList;
	}

	@Override
	public int getCount() {
		if (receiverList == null) {
			return 0;
		} else {
			return receiverList.size();
		}

	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.receiver_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.portraitImageView = (ImageView) convertView
					.findViewById(R.id.imageView_receiver);
			viewHolder.relationshipTextView = (TextView) convertView
					.findViewById(R.id.textView_receiver);
			viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar_receiver);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		RecieverDto recieverDto = receiverList.get(position);
		viewHolder.relationshipTextView.setText(recieverDto.getRelationship());
		viewHolder.progressBar.setVisibility(View.GONE);
		FileTools.loadImage(viewHolder.portraitImageView,
				recieverDto.getFilepath(), Constants.RECEIVER_PORTRAIT,
				Utils.getFileNameFromPath(recieverDto.getFilepath()), viewHolder.progressBar, false);

		return convertView;
	}

	public class ViewHolder {
		public ImageView portraitImageView;
		public TextView relationshipTextView;
		public ProgressBar progressBar;
	}

}
