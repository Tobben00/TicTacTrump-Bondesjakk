package no.woact.martob16;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SetNameDialog extends AppCompatDialogFragment
{
    private EditText setName;
    private SetNameDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_setnamedialog, null);

        builder.setView(view)
                .setTitle("What's your name?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        String playername = setName.getText().toString();
                        listener.applyTexts(playername);
                    }
                });

        setName = view.findViewById(R.id.edittext_setName);

        return builder.create();

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            listener = (SetNameDialogListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " !Hvis eksempel metode returneres må den implementeres!");
        }
    }

    public interface SetNameDialogListener
    {
        void applyTexts(String playername);
    }
}
