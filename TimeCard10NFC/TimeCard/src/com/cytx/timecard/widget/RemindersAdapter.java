package com.cytx.timecard.widget;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import com.cytx.timecard.constants.Constants;
import com.cytx.timecard.R;
import com.cytx.timecard.constants.Constants;
import com.cytx.timecard.dto.HealthStateDto;
import com.cytx.timecard.dto.ReminderDto;

import java.util.List;

import static com.cytx.timecard.R.*;

/**
 * 创建时间：2014年8月19日 下午8:24:18 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：ReminderAdapter.java 类说明：
 */
public class RemindersAdapter extends BaseAdapter {

	private Context context;
    private final Handler handler;
	private List<HealthReminder> reminderDtoList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<HealthReminder> getReminderDtoList() {
        return reminderDtoList;
    }

    public void setReminderDtoList(List<HealthReminder> reminderDtoList) {
        this.reminderDtoList = reminderDtoList;
    }

    public RemindersAdapter(Context context, Handler handler, List<HealthReminder> list) {
		this.context = context;
        this.handler = handler;
		this.reminderDtoList = list;
	}

	@Override
	public int getCount() {
		if (reminderDtoList == null) {
			return 0;
		} else {
			return reminderDtoList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					layout.reminders_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.reminder_desc = (TextView) convertView.findViewById(id.textView_reminder);
            viewHolder.reminder_detail = (TextView) convertView.findViewById(id.textView_detailed_info);
            viewHolder.reminder_time_taken = (TextView) convertView.findViewById(id.textView_time_taken);
            viewHolder.reminder_time_total = (TextView) convertView.findViewById(id.textView_time_total);
            viewHolder.imageView = (ImageView) convertView.findViewById(id.imageView_reminder);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//
//        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                reminderDtoList.get(position).isSelected = !(reminderDtoList.get(position).isSelected);
//
//                if(reminderDtoList.get(position).isSelected )
//                    view.setBackgroundResource(R.drawable.gradient_blue);
//                else
//                    view.setBackgroundResource(drawable.white_grey_border);
//            }
//        });

        viewHolder.imageView.setBackgroundResource(drawable.white_grey_border);
        HealthReminder reminderDto = reminderDtoList.get(position);
        viewHolder.reminder_desc.setTextColor(Color.GRAY);
		viewHolder.reminder_desc.setText(reminderDto.getHealthString());
        viewHolder.reminder_detail.setTextColor(Color.GRAY);
        viewHolder.reminder_detail.setText(reminderDto.getDetailedInfo());
        viewHolder.reminder_time_taken.setVisibility(View.GONE);
        viewHolder.reminder_time_total.setTextColor(Color.GRAY);
        viewHolder.reminder_time_total.setText(String.valueOf(reminderDto.getTimeTotal()));

		return convertView;
	}

    public int getClickedNum() {
        int count = 0;
        for(HealthReminder item : reminderDtoList)
        {
            count += item.isSelected()?1:0;
        }
        return count;
    }

    public class ViewHolder {
		public TextView reminder_desc;
        public ImageView imageView;
        public TextView reminder_detail;
        public TextView reminder_time_taken;
        public TextView reminder_time_total;
	}

}
