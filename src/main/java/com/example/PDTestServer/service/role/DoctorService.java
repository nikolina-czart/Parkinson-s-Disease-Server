package com.example.PDTestServer.service.role;

import com.example.PDTestServer.controller.analysis.request.AnalysisRequestDTO;
import com.example.PDTestServer.controller.role.doctor.request.*;
import com.example.PDTestServer.controller.role.user.request.UserDTO;
import com.example.PDTestServer.model.analysis.fingerTapping.TappingAnalysisData;
import com.example.PDTestServer.model.analysis.gyroscope.TremorAnalysisData;
import com.example.PDTestServer.model.role.UserDAO;
import com.example.PDTestServer.model.tests.TestDetailsDAO;
import com.example.PDTestServer.repository.role.DoctorRepository;
import com.example.PDTestServer.repository.tests.patient.PatientTestsRepository;
import com.example.PDTestServer.repository.role.UserRepository;
import com.example.PDTestServer.service.analysis.AnalysisFingerTappingService;
import com.example.PDTestServer.service.analysis.AnalysisTremorService;
import com.example.PDTestServer.utils.enums.PeriodName;
import com.example.PDTestServer.utils.enums.TestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.example.PDTestServer.utils.coverter.PatientConverter.coverterUserDAOToPatientDTO;
import static com.example.PDTestServer.utils.coverter.TestConverter.convertTestDetailsDAOToTestDTO;
import static com.example.PDTestServer.utils.coverter.UserConverter.convertUserDAOToUserDTO;

@Service
public class DoctorService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientTestsRepository patientTestsRepository;
    @Autowired
    AnalysisFingerTappingService analysisFingerTappingService;
    @Autowired
    AnalysisTremorService analysisTremorService;

    public boolean checkRole(String uid) throws ExecutionException, InterruptedException {
        UserDTO userDTO = convertUserDAOToUserDTO(userRepository.getUserDetails(uid));
        return userDTO.getRole().equals("DOCTOR");
    }

    public Set<PatientDTO> getDoctorPatients(String uid) throws ExecutionException, InterruptedException {
        Set<UserDAO> patientsByDoctorId = doctorRepository.getPatientsByDoctorId(uid);
        return getPatientsWithTests(patientsByDoctorId);
    }

    public List<PatientsDetails> getPatientDetails(String uid) throws ExecutionException, InterruptedException {
        List<PatientsDetails> results = new ArrayList<>();

        String patientsPD = doctorRepository.getNumberPatientPD(uid);
        String controls = doctorRepository.getNumberControls(uid);

        String fingerTappingPatientPD = doctorRepository.getNumberTests(uid, false, TestName.FINGER_TAPPING);
        String fingerTappingControls = doctorRepository.getNumberTests(uid, true, TestName.FINGER_TAPPING);

        String tremorPatientPD = doctorRepository.getNumberTests(uid, false, TestName.TREMORS);
        String tremorControls = doctorRepository.getNumberTests(uid, true, TestName.TREMORS);

        results.add(getPatientsDetailsSummary("Number of patients", patientsPD, controls));
        results.add(getPatientsDetailsSummary("Number of tests: finger tapping", fingerTappingPatientPD, fingerTappingControls));
        results.add(getPatientsDetailsSummary("Number of tests: hand tremor", tremorPatientPD, tremorControls));

        return results;
    }

    public List<SummaryPatients> getPatientSummary(String uid) throws ExecutionException, InterruptedException {
        List<SummaryPatients> results = new ArrayList<>();
        Set<UserDAO> patientsPD = doctorRepository.getPatientsByDoctorIdByControlGroup(uid, false);
        Set<UserDAO> controls = doctorRepository.getPatientsByDoctorIdByControlGroup(uid, true);

        AnalysisRequestDTO analysisRequest = AnalysisRequestDTO.builder()
                .period(PeriodName.ALL.name)
                .build();

        List<TappingAnalysisData> tappingPatientsPD = new ArrayList<>();
        List<TappingAnalysisData> tappingControls = new ArrayList<>();
        List<TremorAnalysisData> tremorPatientsPD = new ArrayList<>();
        List<TremorAnalysisData> tremorControls = new ArrayList<>();

        patientsPD.forEach(patient -> {
            try {
                List<TappingAnalysisData> tappingData = analysisFingerTappingService.getChartDate(patient.getUid(), analysisRequest);
                List<TremorAnalysisData> tremorData = analysisTremorService.getChartDate(patient.getUid(), analysisRequest);
                tappingPatientsPD.addAll(tappingData);
                tremorPatientsPD.addAll(tremorData);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        controls.forEach(patient -> {
            try {
                List<TappingAnalysisData> tappingData = analysisFingerTappingService.getChartDate(patient.getUid(), analysisRequest);
                List<TremorAnalysisData> tremorData = analysisTremorService.getChartDate(patient.getUid(), analysisRequest);
                tappingControls.addAll(tappingData);
                tremorControls.addAll(tremorData);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        results.add(getPatientSummaryData("Patients with PD - Before medicines", tappingPatientsPD, tremorPatientsPD));
        results.add(getPatientSummaryDataAfterMed("Patients with PD - After medication", tappingPatientsPD, tremorPatientsPD));
        results.add(getPatientSummaryData("Control patients", tappingControls, tremorControls));

        return results;
    }

    private SummaryPatients getPatientSummaryDataAfterMed(String group, List<TappingAnalysisData> tappingPatientsPD, List<TremorAnalysisData> tremorPatientsPD) {
        return SummaryPatients.builder()
                .group(group)
                .data(createSummaryParameterAfterMed(tappingPatientsPD, tremorPatientsPD))
                .build();
    }

    private SummaryPatients getPatientSummaryData(String group, List<TappingAnalysisData> tappingPatientsPD, List<TremorAnalysisData> tremorPatientsPD) {
        return SummaryPatients.builder()
                .group(group)
                .data(createSummaryParameter(tappingPatientsPD, tremorPatientsPD))
                .build();
    }

    private SummaryParameter createSummaryParameterAfterMed(List<TappingAnalysisData> tappingPatientsPD, List<TremorAnalysisData> tremorPatientsPD) {
        List<Double> ttDataLeft = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getTouchTime().getDataAfterMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> ttDataRight = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getTouchTime().getDataAfterMedRight().stream())
                .collect(Collectors.toList());

        List<Double> upDataLeft = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getUpTime().getDataAfterMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> upDataRight = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getUpTime().getDataAfterMedRight().stream())
                .collect(Collectors.toList());

        List<Double> iitDataLeft = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getIntertapInterval().getDataAfterMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> iitDataRight = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getIntertapInterval().getDataAfterMedRight().stream())
                .collect(Collectors.toList());

        List<Double> xDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayX().getDataAfterMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> xDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayX().getDataAfterMedRight().stream())
                .collect(Collectors.toList());

        List<Double> yDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayY().getDataAfterMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> yDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayY().getDataAfterMedRight().stream())
                .collect(Collectors.toList());

        List<Double> zDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayZ().getDataAfterMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> zDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayZ().getDataAfterMedRight().stream())
                .collect(Collectors.toList());

        List<Double> aggregatedDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getAggregatedMeanByDay().getDataAfterMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> aggregatedDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getAggregatedMeanByDay().getDataAfterMedRight().stream())
                .collect(Collectors.toList());

        List<Double> differenceXDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayX().getDataAfterMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> differenceXDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayX().getDataAfterMedRight().stream())
                .collect(Collectors.toList());

        List<Double> differenceYDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayY().getDataAfterMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> differenceYDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayY().getDataAfterMedRight().stream())
                .collect(Collectors.toList());

        List<Double> differenceZDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayZ().getDataAfterMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> differenceZDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayZ().getDataAfterMedRight().stream())
                .collect(Collectors.toList());

        return SummaryParameter.builder()
                .touchTime(new SummaryParameterDetails(ttDataLeft, ttDataRight))
                .upTime(new SummaryParameterDetails(upDataLeft, upDataRight))
                .intertapInterval(new SummaryParameterDetails(iitDataLeft, iitDataRight))
                .meanX(new SummaryParameterDetails(xDataLeft, xDataRight))
                .meanY(new SummaryParameterDetails(yDataLeft, yDataRight))
                .meanZ(new SummaryParameterDetails(zDataLeft, zDataRight))
                .aggregated(new SummaryParameterDetails(aggregatedDataLeft, aggregatedDataRight))
                .differenceX(new SummaryParameterDetails(differenceXDataLeft, differenceXDataRight))
                .differenceY(new SummaryParameterDetails(differenceYDataLeft, differenceYDataRight))
                .differenceZ(new SummaryParameterDetails(differenceZDataLeft, differenceZDataRight))
                .build();
    }

    private SummaryParameter createSummaryParameter(List<TappingAnalysisData> tappingPatientsPD, List<TremorAnalysisData> tremorPatientsPD) {
        List<Double> ttDataLeft = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getTouchTime().getDataBeforeMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> ttDataRight = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getTouchTime().getDataBeforeMedRight().stream())
                .collect(Collectors.toList());

        List<Double> upDataLeft = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getUpTime().getDataBeforeMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> upDataRight = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getUpTime().getDataBeforeMedRight().stream())
                .collect(Collectors.toList());

        List<Double> iitDataLeft = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getIntertapInterval().getDataBeforeMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> iitDataRight = tappingPatientsPD.stream()
                .flatMap(data -> data.getData().getIntertapInterval().getDataBeforeMedRight().stream())
                .collect(Collectors.toList());

        List<Double> xDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayX().getDataBeforeMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> xDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayX().getDataBeforeMedRight().stream())
                .collect(Collectors.toList());

        List<Double> yDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayY().getDataBeforeMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> yDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayY().getDataBeforeMedRight().stream())
                .collect(Collectors.toList());

        List<Double> zDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayZ().getDataBeforeMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> zDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getMeanByDayZ().getDataBeforeMedRight().stream())
                .collect(Collectors.toList());

        List<Double> aggregatedDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getAggregatedMeanByDay().getDataBeforeMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> aggregatedDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getAggregatedMeanByDay().getDataBeforeMedRight().stream())
                .collect(Collectors.toList());

        List<Double> differenceXDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayX().getDataBeforeMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> differenceXDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayX().getDataBeforeMedRight().stream())
                .collect(Collectors.toList());

        List<Double> differenceYDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayY().getDataBeforeMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> differenceYDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayY().getDataBeforeMedRight().stream())
                .collect(Collectors.toList());

        List<Double> differenceZDataLeft = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayZ().getDataBeforeMedLeft().stream())
                .collect(Collectors.toList());
        List<Double> differenceZDataRight = tremorPatientsPD.stream()
                .flatMap(data -> data.getData().getDifferenceMeanByDayZ().getDataBeforeMedRight().stream())
                .collect(Collectors.toList());

        return SummaryParameter.builder()
                .touchTime(new SummaryParameterDetails(ttDataLeft, ttDataRight))
                .upTime(new SummaryParameterDetails(upDataLeft, upDataRight))
                .intertapInterval(new SummaryParameterDetails(iitDataLeft, iitDataRight))
                .meanX(new SummaryParameterDetails(xDataLeft, xDataRight))
                .meanY(new SummaryParameterDetails(yDataLeft, yDataRight))
                .meanZ(new SummaryParameterDetails(zDataLeft, zDataRight))
                .aggregated(new SummaryParameterDetails(aggregatedDataLeft, aggregatedDataRight))
                .differenceX(new SummaryParameterDetails(differenceXDataLeft, differenceXDataRight))
                .differenceY(new SummaryParameterDetails(differenceYDataLeft, differenceYDataRight))
                .differenceZ(new SummaryParameterDetails(differenceZDataLeft, differenceZDataRight))
                .build();
    }

    private PatientsDetails getPatientsDetailsSummary(String parameter, String patientsPD, String controls) {
        return PatientsDetails.builder()
                .parameter(parameter)
                .patientPD(patientsPD)
                .patientControl(controls)
                .build();
    }

    private Set<PatientDTO> getPatientsWithTests(Set<UserDAO> patients) {
        Set<PatientDTO> patientsWithTests = new HashSet<>();
        patients.forEach(patient -> patientsWithTests.add(coverterUserDAOToPatientDTO(patient, getTestsByPatient(patient.getUid()))));

        return patientsWithTests;
    }

    private Set<TestDetailDTO> getTestsByPatient(String uid){
        Set<TestDetailDTO> patientTestsDetail = new HashSet<>();

        try{
            Map<String, TestDetailsDAO> dataTestToTestDetails = patientTestsRepository.getTestByUser(uid);
            dataTestToTestDetails.forEach((key, value) -> patientTestsDetail.add(convertTestDetailsDAOToTestDTO(value, key)));
            return patientTestsDetail;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
