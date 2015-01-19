package com.cytx.timecard.widget;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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

    private final Handler handler;
    private Context context;
	private List<HealthStateDto> reminderDtoList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<HealthStateDto> getReminderDtoList() {
        return reminderDtoList;
    }

    public void setReminderDtoList(List<HealthStateDto> reminderDtoList) {
        this.reminderDtoList = reminderDtoList;
    }

    public RemindersAdapter(Context context, Handler handler, List<HealthStateDto> list) {
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
            viewHolder.imageView = (ImageView) convertView.findViewById(id.imageView_reminder);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminderDtoList.get(position).isSelected = !(reminderDtoList.get(position).isSelected);

                if(reminderDtoList.get(position).isSelected )
                    view.setBackgroundResource(R.drawable.gradient_blue);
                else
                    view.setBackgroundResource(drawable.white_grey_border);

                handler.sendEmptyMessage(Constants.MESSAGE_UPDATE_CONFIRM_BUTTON);
            }
        });

        HealthStateDto reminderDto = reminderDtoList.get(position);
		viewHolder.reminder_desc.setText(reminderDto.getReminder());

		return convertView;
	}

    public int getClickedNum() {
        int count = 0;
        for(HealthStateDto item : reminderDtoList)
        {
            count += item.isSelected()?1:0;
        }
        return count;
    }

    public class ViewHolder {
		public TextView reminder_desc;
        public ImageView imageView;
	}

}
