package com.example.design;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SyntaxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SyntaxFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SyntaxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SyntaxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SyntaxFragment newInstance(String param1, String param2) {
        SyntaxFragment fragment = new SyntaxFragment();
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
TextView synfragtext;
    Button copyBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_syntax, container, false);
        synfragtext = view.findViewById(R.id.synfragtext);
        synfragtext.setText(Dictionary.getSyntax());
        copyBtn= view.findViewById(R.id.copyBtn);
        copyBtn.setOnClickListener(View->{
            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("textview", synfragtext.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Text Copied", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.programiz.com/java-programming/online-compiler/"));
            startActivity(in);
        });
        return view;
    }
}