package com.gunt.rxpractice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gunt.rxpractice.databinding.ActivityMainBinding
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.reactivestreams.Publisher

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setContentView(binding.root)

    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    fun exampleObserver() {
        val list: MutableList<Task> = ArrayList()
        list.add(Task("Take out the trash", true, 3))
        list.add(Task("Walk the dog", false, 2))
        list.add(Task("Make my bed", true, 1))
        list.add(Task("Unload the dishwasher", false, 0))
        list.add(Task("Make dinner", true, 5))
        val taskObservable: Observable<Task> =
            Observable.fromIterable(list)
                .subscribeOn(Schedulers.io())
                .filter { it.isComplete }
                .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable) {
                Log.d("TAG", "OnSubscribe : Called!")
                compositeDisposable.add(d)
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

    fun exampleCreateObserver() {
        val task = Task("Walk the dog", false, 3)

        val taskObservable = Observable
            .create(ObservableOnSubscribe<Task> {
                if (!it.isDisposed) {
                    it.onNext(task)
                    it.onComplete()
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Task) {
                Log.d("RX"," OnNext : ${t.description}")
            }

            override fun onError(e: Throwable) {
            }
        })
    }

    fun createListObserver(){
        val tasks = createTasksList()
        val tasksObservable = Observable
            .create(ObservableOnSubscribe<Task> {
                for (task in tasks){
                    if (!it.isDisposed) it.onNext(task)
                }
                if (!it.isDisposed) it.onComplete()
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        tasksObservable.subscribe(object : Observer<Task>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Task) {
                Log.d("Rx", "List OnNext: ${t.description}")
            }

            override fun onError(e: Throwable) {
            }
        })

    }

}