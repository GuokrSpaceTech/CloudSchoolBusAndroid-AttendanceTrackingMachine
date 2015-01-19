package com.cytx.timecard.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cytx.timecard.MainActivity;
import com.cytx.timecard.R;
import com.cytx.timecard.dto.HealthReminder;

import java.util.List;

import static com.cytx.timecard.R.drawable;
import static com.cytx.timecard.R.id;
import static com.cytx.timecard.R.layout;

/**
 * 创建时间：2014年8月19日 下午8:24:18 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：ReminderAdapter.java 类说明：
 */
public class HealthRemindersAdapter extends BaseAdapter {

	private Context context;
	private List<HealthReminder> reminderDtoList;
    private int     originalColor = drawable.confirm_white;

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

    public HealthRemindersAdapter(Context context, List<HealthReminder> list) {
		this.context = context;
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
                boolean hasSelection = false;

                ImageView imageView = ((MainActivity) context).getConfirmImageView();

                reminderDtoList.get(position).isSelected = !(reminderDtoList.get(position).isSelected);

                if(reminderDtoList.get(position).isSelected )
                    view.setBackgroundResource(drawable.gradient_red);
                else
                    view.setBackgroundResource(drawable.white_grey_border);

                //if any of the item is selected, the confirm button turn to blue
                for(int i=0; i<reminderDtoList.size(); i++)
                {
                    if(reminderDtoList.get(i).isSelected)
                        hasSelection = true;
                }

                Object tag = imageView.getTag();
                int id = tag == null ? -1 : Integer.parseInt(tag.toString());
                //Selected Health Status
                if(hasSelection) {
                    switch (id)
                    {
                        case drawable.confirm_white:
                        case drawable.confirm_blue:
                            imageView.setBackgroundResource(drawable.confirm_red);
                            imageView.setTag(drawable.confirm_red);
                            break;
                        case drawable.confirm_red:
                        default:
                            break;
                    }
                }
                //No health Status
                else
                {
                    switch (id)
                    {
                        case drawable.confirm_red:
                            if( ((MainActivity)context).isHasReminders() ) {
                                imageView.setBackgroundResource(drawable.confirm_blue);
                                imageView.setTag(drawable.confirm_blue);
                            }
                            else
                            {
                                imageView.setBackgroundResource(drawable.confirm_white);
                                imageView.setTag(drawable.confirm_white);
                            }
                            break;
                        case drawable.confirm_blue:
                        case drawable.confirm_white:
                        default:
                            break;
                    }
                }
            }
        });

        HealthReminder reminderDto = reminderDtoList.get(position);
		viewHolder.reminder_desc.setText(reminderDto.getHealthString());

		return convertView;
	}

	public class ViewHolder {
		public TextView reminder_desc;
        public ImageView imageView;
	}

}
