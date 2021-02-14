package ru.job4j.srp;

import com.google.gson.Gson;

import java.util.function.Predicate;

public class JSONReport implements Report {
    private Store store;

    public JSONReport(Store store) {
        this.store = store;
    }

    @Override
    public String generate(Predicate<Employee> filter) {
        Gson gson = new Gson();
        StringBuilder builder = new StringBuilder();
        for (Employee employee : store.findBy(filter)) {
            builder.append(gson.toJson(employee));
        }
        return builder.toString();
    }

}
