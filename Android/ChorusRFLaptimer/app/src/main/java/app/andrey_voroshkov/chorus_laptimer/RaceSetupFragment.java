package app.andrey_voroshkov.chorus_laptimer;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.LinearLayout;
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
    private Handler mSessionHandler;
    public boolean isSessionStarted = false;
    public CountDownTimer timer;



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
        updateLiPoMonitorCheckbox(rootView);
        updateBatteryProgressIndicator(rootView);




        AppState.getInstance().addListener(new IDataListener() {
            @Override
            public void onDataChange(DataAction dataItemName) {
                switch (dataItemName) {
                    case RaceMinLap:
                    case RaceLaps:
                    case PreparationTime:
                    case VoltageAdjustmentConst:
                        updateText(rootView);
                        break;
                    case TimedRaceTime:
                        updateText(rootView);
                        break;
                    case SessionTime:
                        updateText(rootView);
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
                    case BatteryVoltage:
                        updateBatteryProgressIndicator(rootView);
                        updateBatteryVoltageText(rootView);
                        break;
                    case LiPoMonitorEnable:
                        updateLiPoMonitorCheckbox(rootView);
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
        Button btnDecSessionTime = (Button) rootView.findViewById(R.id.btnDecSessionTime);
        Button btnIncSessionTime = (Button) rootView.findViewById(R.id.btnIncSessionTime);
        Button btnStartSession = (Button) rootView.findViewById(R.id.btnStartSession);
        Button btnEndSession = (Button) rootView.findViewById(R.id.btnEndSession);
        Button btnDecPrepTime = (Button) rootView.findViewById(R.id.btnDecPreparationTime);
        Button btnIncPrepTime = (Button) rootView.findViewById(R.id.btnIncPreparationTime);
        CheckBox chkSkipFirstLap = (CheckBox) rootView.findViewById(R.id.chkSkipFirstLap);
        CheckBox chkSpeakLapTimes = (CheckBox) rootView.findViewById(R.id.chkSpeakLapTimes);
        CheckBox chkSpeakMessages = (CheckBox) rootView.findViewById(R.id.chkSpeakMessages);
        CheckBox chkDeviceSoundEnabled = (CheckBox) rootView.findViewById(R.id.chkDeviceSoundEnabled);
        CheckBox chkTimedRace = (CheckBox) rootView.findViewById(R.id.chkTimedRace);
        CheckBox chkLiPoMonitor = (CheckBox) rootView.findViewById(R.id.chkLiPoMonitor);
        Button btnDecAdjust = (Button) rootView.findViewById(R.id.btnDecAdjustmentConst);
        Button btnIncAdjust = (Button) rootView.findViewById(R.id.btnIncAdjustmentConst);
        TextView txtVoltage = (TextView) rootView.findViewById(R.id.txtVoltage);
        TextView txtTimer = (TextView) rootView.findViewById(R.id.txtTimer);



        mSessionHandler = new Handler () {
            public void handleMessage(Message msg) {
                int interval = msg.what;
                if (interval == 5) {
                    AppState.getInstance().speakMessage("5 minutes remaining for this group");
                    this.sendEmptyMessageDelayed(4,60000);

                }
                else if (interval == 4) {
                    AppState.getInstance().speakMessage("4 minutes remaining for this group");
                    this.sendEmptyMessageDelayed(3,60000);
                }
                else if (interval == 3) {
                    AppState.getInstance().speakMessage("3 minutes remaining for this group");
                    this.sendEmptyMessageDelayed(2,60000);
                }
                else if (interval == 2) {
                    AppState.getInstance().speakMessage("2 minutes remaining for this group");
                    this.sendEmptyMessageDelayed(1,60000);
                }
                else if (interval == 1) {
                    AppState.getInstance().speakMessage("1 minute remaining for this group");
                    this.sendEmptyMessageDelayed(30,30000);

                }
                else if (interval == 30) {
                    AppState.getInstance().speakMessage("30 seconds remaining for this group. Please land and power off now. ");
                    this.sendEmptyMessageDelayed(11,30000);
                }
                else if (interval == 11) {
                    AppState.getInstance().speakMessage("1 minute intermission starts now. Next group prepare to take off in one minute.   ");
                    AppState.getInstance().playTone(AppState.TONE_GO, AppState.DURATION_SESSION);
                    this.sendEmptyMessageDelayed(22,30000);
                }
                else if (interval == 22) {
                    AppState.getInstance().speakMessage("30 seconds until next group start.");
                    this.sendEmptyMessageDelayed(33,10000);
                }
                else if (interval == 33) {
                    AppState.getInstance().speakMessage("20 seconds until next group start.");
                    this.sendEmptyMessageDelayed(44,10000);
                }
                else if (interval == 44) {
                    AppState.getInstance().speakMessage("10 seconds until next group start.");
                    this.sendEmptyMessageDelayed(0,10000);
                }

                else if (interval == 0) {

                    AppState.getInstance().speakMessage("Intermission is finished. Next group begins now. ");
                    AppState.getInstance().playTone(AppState.TONE_GO, AppState.DURATION_SESSION);
                    //double sessionSeconds = AppState.getInstance().sessionTime * 60000;
                    //mSessionHandler.sendEmptyMessage((int) sessionSeconds);
                    startTimer(rootView);


                }
                else {
                    //send first voice alert 5 minute before race ends
                    double dbl_interval = (double) interval;

                    double mins = dbl_interval / 60000;

                    AppState.getInstance().speakMessage(String.valueOf(mins) + " minute Group session started");
                    int next_interval = interval - 360000;
                    this.sendEmptyMessageDelayed(5, next_interval );



                }


            }

        };

        LinearLayout layoutVoltage = (LinearLayout) rootView.findViewById(R.id.layoutVoltage);

        btnDecAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adj = AppState.getInstance().batteryAdjustmentConst;
                AppState.getInstance().changeAdjustmentConst(adj - 1);
            }
        });

        btnIncAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adj = AppState.getInstance().batteryAdjustmentConst;
                AppState.getInstance().changeAdjustmentConst(adj + 1);
            }
        });

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
                if (time == 1) {
                    new_time = 1.5;
                }
                if (time == 1.5) {
                    new_time = 2;
                }
                if (time == 2) {
                    new_time = 2.5;
                }
                AppState.getInstance().changeTimedRaceTime(new_time);
            }}
        );

        btnDecSessionTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double time = AppState.getInstance().sessionTime;
                double new_time = time;
                if (time > 1 && time <= 10 )
                {
                    new_time = time - 1;
                }
                AppState.getInstance().changeSessionTime(new_time);
            }
        });

        btnIncSessionTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double time = AppState.getInstance().sessionTime;
                double new_time = time;
                if (time > 1 && time < 10 )
                {
                    new_time = time + 1;
                }
                AppState.getInstance().changeSessionTime(new_time);
            }}
        );

        btnStartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSessionStarted == false) {
                    isSessionStarted = true;
                    startTimer(rootView);
                }




            }}
        );



        btnEndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView txtTimer = (TextView) rootView.findViewById(R.id.txtTimer);
                if (isSessionStarted == true)
                {
                    isSessionStarted = false;
                    mSessionHandler.removeCallbacksAndMessages(null);
                    timer.cancel();
                    timer = null;
                    txtTimer.setText("cancelled");
                }
            }}
        );

        chkLiPoMonitor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppState.getInstance().changeEnableLiPoMonitor(isChecked);
            }
        });

        txtVoltage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toggleVoltageAdjustmentControls(rootView);
                return false;
            }
        });

        layoutVoltage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toggleVoltageAdjustmentControls(rootView);
                return false;
            }
        });

        return rootView;
    }

    private void toggleVoltageAdjustmentControls(View rootView) {
        boolean isEnabled = AppState.getInstance().isLiPoMonitorEnabled;
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.adjustmentLayout);
        boolean isVisible = layout.getVisibility() == View.VISIBLE;
        if (!isVisible) {
            if (isEnabled) {
                layout.setVisibility(View.VISIBLE);
            }
        } else {
            layout.setVisibility(View.GONE);
        }
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

        TextView txtSessionTime = (TextView) rootView.findViewById(R.id.txtSessionTime);
        txtSessionTime.setText(Double.toString(AppState.getInstance().sessionTime) + " min.");

        TextView txtAdjustmentConst = (TextView) rootView.findViewById(R.id.txtAdjustmentConst);
        txtAdjustmentConst.setText(Integer.toString(AppState.getInstance().batteryAdjustmentConst));
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

    private void updateLiPoMonitorCheckbox(View rootView) {
        boolean isEnabled = AppState.getInstance().isLiPoMonitorEnabled;
        CheckBox chkLiPoMonitor = (CheckBox) rootView.findViewById(R.id.chkLiPoMonitor);
        chkLiPoMonitor.setChecked(isEnabled);
        LinearLayout layoutVoltage = (LinearLayout) rootView.findViewById(R.id.layoutVoltage);
        layoutVoltage.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
        LinearLayout layoutAdjustment = (LinearLayout) rootView.findViewById(R.id.adjustmentLayout);
        layoutAdjustment.setVisibility(View.GONE);
    }

    private void updateBatteryProgressIndicator(View rootView) {
        ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.batteryCharge);
        int percent = AppState.getInstance().batteryPercentage;
        bar.setProgress(percent);
        int colorId = (percent > 10) ? (percent > 20) ? R.color.colorPrimary : R.color.colorWarn: R.color.colorAccent;
        int color = ContextCompat.getColor(mContext, colorId);
        bar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    private void updateBatteryVoltageText(View rootView) {
        TextView txtVoltage = (TextView) rootView.findViewById(R.id.txtVoltage);
        txtVoltage.setText(String.format("%.2f", AppState.getInstance().batteryVoltage) + "V");

    }

    private void startTimer(View rootView) {
        {
            final TextView txtTimer = (TextView) rootView.findViewById(R.id.txtTimer);

            double sessionSeconds = AppState.getInstance().sessionTime * 60000;
            int timerSeconds = (int) sessionSeconds;
            mSessionHandler.sendEmptyMessage((int) sessionSeconds);


            timer = new CountDownTimer(timerSeconds, 1000) {

                public void onTick(long millisUntilFinished) {
                    long minutes = millisUntilFinished / 1000 / 60;
                    long seconds = millisUntilFinished - (minutes * 60000);
                    txtTimer.setText(millisUntilFinished / 1000 / 60 + ":" + String.format("%02d", seconds / 1000));
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    txtTimer.setText("done!");
                }

            }.start();

        }

    }
}
