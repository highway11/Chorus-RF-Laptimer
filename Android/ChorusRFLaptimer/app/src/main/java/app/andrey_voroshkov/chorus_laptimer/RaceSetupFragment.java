package app.andrey_voroshkov.chorus_laptimer;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RaceSetupFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private View mRootView;
    private Context mContext;

    public RaceSetupFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RaceSetupFragment newInstance(int sectionNumber) {
        RaceSetupFragment fragment = new RaceSetupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.race_setup, container, false);
        mRootView = rootView;
        mContext = getContext();

        updateText(rootView);
        updateSkipFirstLapCheckbox(rootView);
        updateSoundCheckbox(rootView);
        updateSpeakLapTimesCheckbox(rootView);
        updateSpeakMessagesCheckbox(rootView);
        updateTimedRaceCheckbox(rootView);

        AppState.getInstance().addListener(new IDataListener() {
            @Override
            public void onDataChange(DataAction dataItemName) {
                switch (dataItemName) {
                    case RaceMinLap:
                    case RaceLaps:
                    case PreparationTime:
                        updateText(rootView);
                        break;
                    case TimedRaceTime:
                        updateText(rootView);
                        break;
                    case SoundEnable:
                        updateSoundCheckbox(rootView);
                        break;
                    case SkipFirstLap:
                        updateSkipFirstLapCheckbox(rootView);
                        break;
                    case SpeakLapTimes:
                        updateSpeakLapTimesCheckbox(rootView);
                        break;
                    case SpeakMessages:
                        updateSpeakMessagesCheckbox(rootView);
                        break;
                    case TimedRace:
                        updateTimedRaceCheckbox(rootView);
                        break;
                    case BatteryPercentage:
                        updateBatteryProgressIndicator(rootView);
                        break;
                }
            }
        });

        Button btnDecMLT = (Button) rootView.findViewById(R.id.btnDecMinLapTime);
        Button btnIncMLT = (Button) rootView.findViewById(R.id.btnIncMinLapTime);
        Button btnDecLaps = (Button) rootView.findViewById(R.id.btnDecLaps);
        Button btnIncLaps = (Button) rootView.findViewById(R.id.btnIncLaps);
        Button btnDecRaceTime = (Button) rootView.findViewById(R.id.btnDecRaceTime);
        Button btnIncRaceTime = (Button) rootView.findViewById(R.id.btnIncRaceTime);
        Button btnDecPrepTime = (Button) rootView.findViewById(R.id.btnDecPreparationTime);
        Button btnIncPrepTime = (Button) rootView.findViewById(R.id.btnIncPreparationTime);
        CheckBox chkSkipFirstLap = (CheckBox) rootView.findViewById(R.id.chkSkipFirstLap);
        CheckBox chkSpeakLapTimes = (CheckBox) rootView.findViewById(R.id.chkSpeakLapTimes);
        CheckBox chkSpeakMessages = (CheckBox) rootView.findViewById(R.id.chkSpeakMessages);
        CheckBox chkDeviceSoundEnabled = (CheckBox) rootView.findViewById(R.id.chkDeviceSoundEnabled);
        CheckBox chkTimedRace = (CheckBox) rootView.findViewById(R.id.chkTimedRace);

        btnDecMLT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppState.getInstance().sendBtCommand("R*m");
            }
        });

        btnIncMLT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppState.getInstance().sendBtCommand("R*M");
            }
        });

        btnDecLaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int laps = AppState.getInstance().raceState.lapsToGo;
                AppState.getInstance().changeRaceLaps(laps - 1);
            }
        });

        btnIncLaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int laps = AppState.getInstance().raceState.lapsToGo;
                AppState.getInstance().changeRaceLaps(laps + 1);
            }
        });

        btnDecPrepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = AppState.getInstance().timeToPrepareForRace;
                AppState.getInstance().changeTimeToPrepareForRace(time - 1);
            }
        });

        btnIncPrepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = AppState.getInstance().timeToPrepareForRace;
                AppState.getInstance().changeTimeToPrepareForRace(time + 1);
            }
        });



        chkDeviceSoundEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppState.getInstance().sendBtCommand("R*D");
            }
        });

        chkDeviceSoundEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(AppState.getInstance().isDeviceSoundEnabled);
            }
        });

        chkSkipFirstLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppState.getInstance().sendBtCommand("R*F");
            }
        });

        chkSkipFirstLap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(AppState.getInstance().shouldSkipFirstLap);
            }
        });

        chkSpeakLapTimes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppState.getInstance().changeShouldSpeakLapTimes(isChecked);
            }
        });

        chkSpeakMessages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppState.getInstance().changeShouldSpeakMessages(isChecked);
            }
        });

        chkTimedRace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppState.getInstance().changeTimedRace(isChecked);
            }
        });

        btnDecRaceTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double time = AppState.getInstance().timedRaceTime;
                double new_time = time;
                if (time == 1.5)
                {
                    new_time = 1;
                }
                if (time == 2)
                {
                    new_time = 1.5;
                }
                if(time == 2.5)
                {
                    new_time = 2;
                }
                AppState.getInstance().changeTimedRaceTime(new_time);
            }
        });

        btnIncRaceTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double time = AppState.getInstance().timedRaceTime;
                double new_time = time;
                if (time == 1)
                {
                    new_time = 1.5;
                }
                if (time == 1.5)
                {
                    new_time = 2;
                }
                if(time == 2)
                {
                    new_time = 2.5;
                }
                AppState.getInstance().changeTimedRaceTime(new_time);
            }
        });

        return rootView;
    }

    private void updateText(View rootView) {
        TextView txtMinLaps = (TextView) rootView.findViewById(R.id.txtMinLapTime);
        txtMinLaps.setText(Integer.toString(AppState.getInstance().raceState.minLapTime) + " sec.");

        TextView txtLaps = (TextView) rootView.findViewById(R.id.txtLaps);
        txtLaps.setText(Integer.toString(AppState.getInstance().raceState.lapsToGo));

        TextView txtPreparationTime = (TextView) rootView.findViewById(R.id.txtPreparationTime);
        txtPreparationTime.setText(Integer.toString(AppState.getInstance().timeToPrepareForRace) + " sec.");

        TextView txtRaceTime = (TextView) rootView.findViewById(R.id.txtRaceTime);
        txtRaceTime.setText(Double.toString(AppState.getInstance().timedRaceTime) + " min.");
    }

    private void updateSkipFirstLapCheckbox(View rootView) {
        CheckBox chkSkipFirstLap = (CheckBox) rootView.findViewById(R.id.chkSkipFirstLap);
        chkSkipFirstLap.setChecked(AppState.getInstance().shouldSkipFirstLap);
    }

    private void updateSoundCheckbox(View rootView) {
        CheckBox chkDeviceSoundEnabled = (CheckBox) rootView.findViewById(R.id.chkDeviceSoundEnabled);
        chkDeviceSoundEnabled.setChecked(AppState.getInstance().isDeviceSoundEnabled);
    }

    private void updateSpeakLapTimesCheckbox(View rootView) {
        CheckBox chkSpeakLapTimes = (CheckBox) rootView.findViewById(R.id.chkSpeakLapTimes);
        chkSpeakLapTimes.setChecked(AppState.getInstance().shouldSpeakLapTimes);
    }

    private void updateSpeakMessagesCheckbox(View rootView) {
        CheckBox chkSpeakMessages = (CheckBox) rootView.findViewById(R.id.chkSpeakMessages);
        chkSpeakMessages.setChecked(AppState.getInstance().shouldSpeakMessages);
    }

    private void updateTimedRaceCheckbox(View rootView) {
        CheckBox chkTimedRace = (CheckBox) rootView.findViewById(R.id.chkTimedRace);
        chkTimedRace.setChecked(AppState.getInstance().isTimedRace);
    }

    private void updateBatteryProgressIndicator(View rootView) {
        ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.batteryCharge);
        TextView txt = (TextView) rootView.findViewById(R.id.txtRssi);
        int percent = AppState.getInstance().batteryPercentage;
        bar.setProgress(percent);
        int colorId = (percent > 10) ? (percent > 20) ? R.color.colorPrimary : R.color.colorWarn: R.color.colorAccent;
        int color = ContextCompat.getColor(mContext, colorId);
        bar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}
