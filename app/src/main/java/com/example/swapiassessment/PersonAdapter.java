package com.example.swapiassessment;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private List<Person> people;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

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
        String heightText = person.getHeight().equals("unknown") ? "Height Unknown" : person.getHeight() + "cm";
        String massText = person.getMass().equals("unknown") ? "Mass Unknown" : person.getMass() + "kg";
        String birthYearText = person.getBirthYear().equals("unknown") ? "Birth Year Unknown" : "Born " + person.getBirthYear();

        holder.setName(person.getName());
        holder.setHeight(heightText);
        holder.setMass(massText);
        holder.setBirthYear(birthYearText);
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public Person getPerson(int position) {
        return people.get(position);
    }

    public void addPerson(Person person) {
        people.add(person);
        notifyItemInserted(getItemCount());
    }

    void setClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView personName;
        private TextView personHeight;
        private TextView personMass;
        private TextView personBirthYear;

        ViewHolder(View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.personName);
            personHeight = itemView.findViewById(R.id.personHeight);
            personMass = itemView.findViewById(R.id.personMass);
            personBirthYear = itemView.findViewById(R.id.personBirthYear);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }

        // NOTE: Though I use setters here, please keep in mind that my actual opinion
        // on whether they are necessary is a bit shaky. I am using them because they seem to be
        // standard in Java programming, but whether they should be used to assign to fields with
        // no other processing seems to be a contested issue
        // (see https://stackoverflow.com/questions/1568091/why-use-getters-and-setters-accessors).
        // If we have time, I think this is something I'd like to discuss in the interview.

        public void setName(String name) {
            personName.setText(name);
        }

        public void setHeight(String name) {
            personHeight.setText(name);
        }

        public void setMass(String name) {
            personMass.setText(name);
        }

        public void setBirthYear(String name) {
            personBirthYear.setText(name);
        }

    }

}
