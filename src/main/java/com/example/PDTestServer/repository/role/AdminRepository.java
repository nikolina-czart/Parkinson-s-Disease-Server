package com.example.PDTestServer.repository.role;

import com.example.PDTestServer.controller.role.admin.request.DoctorDTO;
import com.example.PDTestServer.utils.firebase.FieldName;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.example.PDTestServer.utils.firebase.FirebaseQuery.allDoctors;
import static com.example.PDTestServer.utils.firebase.FirebaseQuery.patientByDoctorIdOrGroup;

@Repository
public class AdminRepository {
    public List<DoctorDTO> getAllDoctors() throws ExecutionException, InterruptedException {
        List<DoctorDTO> doctors = new ArrayList<>();
        List<QueryDocumentSnapshot> documents = allDoctors().get().get().getDocuments();
        documents.forEach(doctor -> {
            try {
                doctors.add(createDoctor(doctor));
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return doctors;
    }

    private DoctorDTO createDoctor(QueryDocumentSnapshot doctor) throws ExecutionException, InterruptedException {
        Map<String, Object> doctorData = doctor.getData();
        long patients = patientByDoctorIdOrGroup(doctor.getId(), false).count().get().get().getCount();
        long controls = patientByDoctorIdOrGroup(doctor.getId(), true).count().get().get().getCount();

        return DoctorDTO.builder()
                .uid(doctor.getId())
                .name((String) doctorData.get(FieldName.NAME.name))
                .surname((String) doctorData.get(FieldName.SURNAME.name))
                .email((String) doctorData.get(FieldName.EMAIL.name))
                .controlsNumber(controls)
                .patientsNumber(patients)
                .build();
    }
}
