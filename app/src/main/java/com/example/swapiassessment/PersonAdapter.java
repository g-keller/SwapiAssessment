package com.example.swapiassessment;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private List<Person> people;
    private LayoutInflater inflater;
    // private ItemClickListener clickListener;

    PersonAdapter(Context context, List<Person> people) {
        this.inflater = LayoutInflater.from(context);
        this.people = people;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.person_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = people.get(position);
        holder.label.setText(person.getName());
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder { //} implements View.OnClickListener {

        TextView label;
        ViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.personListItemTextView);
        }

    }

}
