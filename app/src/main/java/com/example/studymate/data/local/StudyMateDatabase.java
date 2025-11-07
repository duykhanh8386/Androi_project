package com.example.studymate.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.studymate.data.local.dao.*;
import com.example.studymate.data.local.entity.*;
import com.example.studymate.utils.HashUtil;

import java.util.concurrent.Executors;

@Database(
    entities = {
        User.class, ClassRoom.class, StudentClass.class,
        Notification.class, Feedback.class, Grade.class
    },
    version = 3,              // ⬅ bump version để reseed nếu cần
    exportSchema = false
)
public abstract class StudyMateDatabase extends RoomDatabase {
    private static volatile StudyMateDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ClassDao classDao();
    public abstract NotificationDao notificationDao();
    public abstract FeedbackDao feedbackDao();
    public abstract GradeDao gradeDao();

    public static StudyMateDatabase getInstance(Context ctx) {
        if (INSTANCE == null) {
            synchronized (StudyMateDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(),
                            StudyMateDatabase.class, "studymate.db")
                        .fallbackToDestructiveMigration()
                        .addCallback(SEED)
                        .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback SEED = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    // Hash mật khẩu demo
                    final String passHash = HashUtil.sha256("123456");

                    // ⚠️ Dùng backtick cho table/column để an toàn tên/từ khóa
                    db.execSQL(
                        "INSERT INTO `user`(username, passwordHash, role, status, disabled, fullName) VALUES " +
                            "('admin',    '" + passHash + "', 'ADMIN',   'ACTIVE', 0, 'Quản trị viên')," +
                            "('teacher1', '" + passHash + "', 'TEACHER', 'ACTIVE', 0, 'GV Toán')," +
                            "('student1', '" + passHash + "', 'STUDENT', 'ACTIVE', 0, 'HS A')"
                    );

                    // Lớp mẫu do giáo viên id=2 phụ trách
                    db.execSQL(
                        "INSERT INTO `class`(code, name, year, maxSize, teacherId) " +
                            "VALUES ('M101', 'Toán 10A1', 2025, 45, 2)"
                    );

                    // Học sinh id=3 là member của class id=1
                    db.execSQL(
                        "INSERT INTO `student_class`(classId, studentId, status) " +
                            "VALUES (1, 3, 'MEMBER')"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };
}
