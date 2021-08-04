package com.example.nav;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_profile newInstance(String param1, String param2) {
        fragment_profile fragment = new fragment_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private EditText editText_name1, editText_name2, editText_contact1, editText_contact2;
    private Button buttonSave;
    private boolean isStateSaved = false;

    String name1, name2, contact1, contact2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editText_name1 = view.findViewById(R.id.textView_nameInput1);
        editText_name2 = view.findViewById(R.id.textView_nameInput2);
        editText_contact1 = view.findViewById(R.id.textView_contact1Input1);
        editText_contact2 = view.findViewById(R.id.textView_contact2Input2);

        buttonSave = view.findViewById(R.id.buttonsave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStateSaved) {
                    saveData();
                }
            }
        });
        loadData();
        updateViews();

        return view;
    }

    private void saveData() {
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences(constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(constants.CONTACT1,editText_contact1.getText().toString().trim());
        editor.putString(constants.CONTACT2,editText_contact2.getText().toString().trim());
        editor.putString(constants.NAME1,editText_name1.getText().toString().trim());
        editor.putString(constants.NAME2,editText_name2.getText().toString().trim());
        editor.apply();


        ((MainActivity) getActivity()).name1 = editText_name1.getText().toString();
        ((MainActivity) getActivity()).name2 = editText_name2.getText().toString();
        ((MainActivity) getActivity()).contact1 = editText_contact1.getText().toString();
        ((MainActivity) getActivity()).contact2 = editText_contact2.getText().toString();
        ((MainActivity) getActivity()).saveData();

        Toast.makeText(getActivity(), "Data Saved", Toast.LENGTH_SHORT).show();
    }

    private void loadData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(constants.SHARED_PREFS,Context.MODE_PRIVATE);
        contact1=sharedPreferences.getString(constants.CONTACT1,"");
        contact2=sharedPreferences.getString(constants.CONTACT2,"");
        name1 = sharedPreferences.getString(constants.NAME1,"");
        name2 = sharedPreferences.getString(constants.NAME2,"");
    }

    private void updateViews()
    {
        editText_contact2.setText(contact2);
        editText_contact1.setText(contact1);
        editText_name1.setText(name1);
        editText_name2.setText(name2);
    }
}