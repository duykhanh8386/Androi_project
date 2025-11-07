// ClassViewModel.java
package com.example.studymate.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.local.entity.ClassRoom;
import com.example.studymate.repository.ClassRepository;
import com.example.studymate.ui.home.model.ClassItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ClassViewModel extends AndroidViewModel {
    private final ClassRepository repo;
    private final MediatorLiveData<List<ClassItem>> classes = new MediatorLiveData<>();
    public final MutableLiveData<ClassRepository.JoinResult> joinResult = new MutableLiveData<>();
    public final MutableLiveData<Boolean> leaveResult = new MutableLiveData<>();

    public ClassViewModel(@NonNull Application app) {
        super(app);
        repo = new ClassRepository(app);
    }

    public LiveData<List<ClassItem>> getClasses() { return classes; }

    // Học sinh: sub list lớp
    public void subscribeStudentClasses(long studentId) {
        classes.addSource(repo.classesOfStudent(studentId), rooms -> {
            List<ClassItem> out = new ArrayList<>();
            if (rooms != null) {
                for (ClassRoom r : rooms) out.add(new ClassItem(r.id, r.name));
            }
            classes.setValue(out);
        });
    }

    // Giáo viên: sub list lớp
    public void subscribeTeacherClasses(long teacherId) {
        classes.addSource(repo.classesOfTeacher(teacherId), rooms -> {
            List<ClassItem> out = new ArrayList<>();
            if (rooms != null) {
                for (ClassRoom r : rooms) out.add(new ClassItem(r.id, r.name));
            }
            classes.setValue(out);
        });
    }

    // ✅ THÊM method này để xử lý “Tham gia lớp bằng mã”
    public void joinByCode(String code, long studentId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            ClassRepository.JoinResult res = repo.joinByCode(code, studentId);
            joinResult.postValue(res);
        });
    }

    public void leave(long classId, long studentId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            boolean ok = repo.leaveClass(classId, studentId);
            leaveResult.postValue(ok);
        });
    }
}
