package com.gunt.rxpractice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gunt.rxpractice.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setContentView(binding.root)

        val taskObservable: Observable<Task> =
            Observable.fromIterable(createTasksList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


        taskObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable) {
                Log.d("TAG", "OnSubscribe : Called!")
            }

            override fun onComplete() {
                Log.d("TAG", "OnComplete : Called!")
            }

            override fun onNext(t: Task) {
                Log.d("TAG", "OnNextThread : ${Thread.currentThread().name}")
                Log.d("TAG", "OnNext : ${t.description}")
            }

            override fun onError(e: Throwable) {
                Log.d("TAG", "OnError : $e")
            }
        })
    }
}