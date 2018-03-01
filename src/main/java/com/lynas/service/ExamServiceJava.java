package com.lynas.service;

import com.lynas.dto.ExamClassResponse1;
import com.lynas.dto.ExamQueryResult;
import com.lynas.model.Exam;
import com.lynas.model.util.ExamType;
import com.lynas.repo.ExamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by seal on 5/28/2017.
 */
@Service
public class ExamServiceJava {

    private static final String GRAND_TOTAL = "GRAND_TOTAL";

    private ExamRepository examRepository;

    public ExamServiceJava(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Transactional
    public List<ExamClassResponse1> getResultOfClass(long classId, int year, long orgId) {
        List<ExamQueryResult> list = examRepository.resultOfClassByYear(classId, year, orgId);

        if (list == null) return new ArrayList<>();

        Map<String, Double> subjectTotalMark = calculateTotal(list);
        double grandTotal = subjectTotalMark.get(GRAND_TOTAL);
        List<ExamClassResponse1> examClassResponse1s = list
                .stream()
                .sorted(Comparator.comparingInt(ExamQueryResult::getRoleNumber))
                .collect(Collectors.groupingBy(ExamQueryResult::getRoleNumber))
                .entrySet()
                .stream()
                .map(i -> {
                    List<ExamQueryResult> examQueryResultList = i.getValue();
                    Integer rollNumber = i.getKey();

                    Map<String, Double> subjectToObtainMarkMap = subjectToObtainMarkMap(examQueryResultList);
                    double obtainTotal = totalObtainMark(subjectToObtainMarkMap);
                    List<ExamClassResponse1.Subjects> subjectsList = subjects(examQueryResultList, subjectToObtainMarkMap, subjectTotalMark);
                    ExamClassResponse1.GrandTotal grandTotalObj = new ExamClassResponse1.GrandTotal(grandTotal, obtainTotal);
                    return new ExamClassResponse1(examQueryResultList.get(0).getStudentId(),
                            classId,
                            rollNumber,
                            year,
                            grandTotalObj,
                            subjectsList);
                })
                .collect(Collectors.toList());
        return examClassResponse1s;
    }

    private static final Function<ExamQueryResult, Double> totalMarkOfSubject = examQueryResult ->
            examQueryResult.getExam()
                    .stream()
                    .mapToDouble(exam -> (exam.getObtainedNumber() * exam.getPercentile()) / 100)
                    .sum();

    private Map<String, Double> subjectToObtainMarkMap(List<ExamQueryResult> examQueryResultList) {
        return examQueryResultList
                .stream()
                .collect(Collectors.toMap(ExamQueryResult::getSubject, totalMarkOfSubject));
    }

    private double totalObtainMark(Map<String, Double> subjectToObtainMarkMap) {
        return subjectToObtainMarkMap
                .values()
                .stream()
                .mapToDouble(Double::valueOf)
                .sum();
    }

    private List<ExamClassResponse1.Subjects> subjects(List<ExamQueryResult> examQueryResultList,
                                                       Map<String, Double> subjectToObtainMarkMap,
                                                       Map<String, Double> subjectTotalMark) {
        return examQueryResultList
                .stream()
                .map(examQueryResult -> subject(examQueryResult, subjectToObtainMarkMap, subjectTotalMark))
                .collect(Collectors.toList());
    }

    private ExamClassResponse1.Subjects subject(ExamQueryResult examQueryResult,
                                                     Map<String, Double> subjectToObtainMarkMap,
                                                     Map<String, Double> subjectTotalMark) {
        ExamClassResponse1.Subjects subjects = new ExamClassResponse1.Subjects();
        subjects.setSubjectName(examQueryResult.getSubject());
        Map<ExamType, Exam> examMap = examTypeToExamMap(examQueryResult);
        double obtainMark = subjectToObtainMarkMap.get(examQueryResult.getSubject());
        subjects.setExams(examMap);
        subjects.setObtained(obtainMark);
        subjects.setTotal(subjectTotalMark.get(examQueryResult.getSubject()));
        return subjects;
    }

    private Map<ExamType, Exam> examTypeToExamMap(ExamQueryResult examQueryResult) {
        return examQueryResult.getExam()
                .stream()
                .collect(Collectors.toMap(Exam::getExamType, Function.identity()));
    }


    private Map<String, Double> calculateTotal(List<ExamQueryResult> list) {
        Map<String, Double> map = list
                .stream()
                .collect(Collectors.toMap(ExamQueryResult::getSubject, p -> p, (p, q) -> p))
                .values()
                .stream()
                .collect(Collectors.toMap(ExamQueryResult::getSubject, i -> i.getExam()
                        .stream()
                        .mapToDouble(j -> (j.getTotalNumber() * j.getPercentile()) / 100).sum()));

        map.put(GRAND_TOTAL, map.entrySet().stream().mapToDouble(Map.Entry::getValue).sum());
        return map;
    }
}
