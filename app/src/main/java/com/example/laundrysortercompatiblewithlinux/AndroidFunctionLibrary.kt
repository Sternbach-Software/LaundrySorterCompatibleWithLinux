import com.example.laundrysortercompatiblewithlinux.mEntireApplicationContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

//import KotlinFunctionLibrary.formatted
//import KotlinFunctionLibrary.toHrMinSec
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.content.res.ColorStateList
//import android.content.res.Resources
//import android.graphics.Color
//import android.graphics.drawable.Drawable
//import android.graphics.drawable.LayerDrawable
//import android.graphics.drawable.RippleDrawable
//import android.os.Build
//import android.os.Parcelable
//import android.util.TypedValue
//import android.view.Menu
//import android.view.MenuInflater
//import android.view.MenuItem
//import android.view.View
//import android.view.inputmethod.EditorInfo
//import android.view.inputmethod.InputMethodManager
//import android.widget.AutoCompleteTextView
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.widget.SearchView
//import androidx.core.content.ContextCompat
//import androidx.core.text.isDigitsOnly
//import androidx.core.view.ViewCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentFactory
//import androidx.fragment.app.FragmentManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.color.MaterialColors
//import com.google.android.material.shape.MaterialShapeDrawable
//import com.google.android.material.snackbar.Snackbar
//import kotlinx.coroutines.*
//import KotlinFunctionLibrary.isInEnum
//import timber.log.Timber
//import java.io.*
//import java.util.*
//import kotlin.reflect.KClass
//import kotlin.reflect.KProperty1
//
//
///**
// * This is a library for functions used throughout the app, such a filter() for a RecyclerView, to facilitate DRYness.
// * */
//object AndroidFunctionLibrary {
//    /**
//     * When the user clicks the cancel button on the [SearchView], by default the first time they
//     * click it, the [SeachView]'s [EditText] is cleared but the [SearchView] remains opened. Only
//     * once they click it again does the [SearchView] close. If this is true, the [SearchView] is
//     * closed and the query is set to "" - thereby updating the list.
//     * */
//    const val userWantsToCancelSearchBarOnFirstCancelButtonClick = true
//
//    /**
//     * I assume that if an activity is calling this function, it is also going to setup the selection button
//     * */
//    fun setupFilterAndSearch(
//        menu: Menu?,
//        menuInflater: MenuInflater,
//        torahFilterableCallback: TorahFilterable,
//        fragmentManager: FragmentManager,
//        mapOfFilterCriteriaData: Map<ShiurFilterOption, List<String>>,
//        callingFromActivity: Boolean,
//        callingActivity: Activity?,
//        contextForClosingKeyboard: Context?,
//        viewForClosingKeyboard: View?
//    ) {
//        if (menu != null) {
//            setupFilterButton(
//                menuInflater,
//                menu,
//                torahFilterableCallback,
//                mapOfFilterCriteriaData,
//                fragmentManager,
//                alsoUsingSearchButton = true,
//                shouldInflateLayout = true
//            )
//            setupSearchView(
//                menuInflater, menu, torahFilterableCallback,
//                alsoUsingFilterButton = true,
//                shouldInflateLayout = false,
//                callingFromActivity,
//                callingActivity,
//                contextForClosingKeyboard,
//                viewForClosingKeyboard
//            )
//        }
//    }
//
//    // Practically, this function is never called without also calling [setupSearchView], but it can
//    // be if need be.
//    fun setupFilterButton(
//        menuInflater: MenuInflater,
//        menu: Menu,
//        torahFilterableCallback: TorahFilterable,
//        mapOfFilterCriteriaData: Map<ShiurFilterOption, List<String>>,
//        fragmentManager: FragmentManager,
//        alsoUsingSearchButton: Boolean,
//        shouldInflateLayout: Boolean
//    ) {
//        if (shouldInflateLayout) menuInflater.inflate(
//            if (alsoUsingSearchButton) R.menu.search_bar_filter_button_and_selection else R.menu.filter_button_only,
//            menu
//        )
//        val filterItem: MenuItem? = menu.findItem(R.id.filter_button)
//        filterItem?.setOnMenuItemClickListener {
//            fragmentManager.fragmentFactory = object : FragmentFactory() {//neccessary because ShiurimSortOrFilterDialog does not have an empty constructor and will throw a FragmentInstantiationException when the system tries to restore the state after process death
//                override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
//                    return if ((loadFragmentClass(
//                            classLoader,
//                            className
//                        ) == ShiurimSortOrFilterDialog::class)) ShiurimSortOrFilterDialog(
//                        torahFilterableCallback,
//                        mapOfFilterCriteriaData
//                    )
//                    else super.instantiate(classLoader, className)
//                }
//            }
//            ShiurimSortOrFilterDialog(
//                torahFilterableCallback,
//                mapOfFilterCriteriaData
//            ).show(fragmentManager, "shiurimSortFilterDialog")
//            true
//        }
//    }
//
//    /**
//     * Sets up [SearchView] in a toolbar.
//     * @param callingFromActivity the steps need to close the keyboard when the user submits are different
//     * for an activity and for a [Fragment]. See https://stackoverflow.com/questions/1109022/how-do-you-close-hide-the-android-soft-keyboard-using-java
//     * @param callingActivity needed for closing keyboard, [null] if not an activity
//     * @param contextForClosingKeyboard ^ [null] if not a [Fragment]
//     * @param viewForClosingKeyboard ^
//     * */
//    fun setupSearchView(
//        menuInflater: MenuInflater,
//        menu: Menu,
//        torahFilterableCallback: TorahFilterable,
//        alsoUsingFilterButton: Boolean,
//        shouldInflateLayout: Boolean,
//        callingFromActivity: Boolean,
//        callingActivity: Activity?,
//        contextForClosingKeyboard: Context?,
//        viewForClosingKeyboard: View?
//
//    ) {
//        if (shouldInflateLayout) menuInflater.inflate(
//            if (alsoUsingFilterButton) R.menu.search_bar_filter_button_and_selection else R.menu.search_bar_only,
//            menu
//        )
//
//        val findItem = menu.findItem(R.id.actionSearch)
//        val searchView = findItem?.actionView as SearchView?
//        searchView?.imeOptions = EditorInfo.IME_ACTION_DONE
//
//
//        if (userWantsToCancelSearchBarOnFirstCancelButtonClick) {
//            // Get the search close button image view
//            val closeButton: ImageView? =
//                searchView?.findViewById(R.id.search_close_btn) as ImageView?
//
//            // Set on click listener
//            closeButton?.setOnClickListener {
//                Timber.d("Search close button clicked")
//                //Find EditText view
//                val editText = searchView?.findViewById(R.id.search_src_text) as EditText?
//
//                //Clear the text from EditText view
//                editText?.setText("")
//
//                //Clear query
//                searchView?.setQuery("", false)
//                //Collapse the action view
//                searchView?.onActionViewCollapsed()
//                //Collapse the search widget
//                findItem.collapseActionView()
//            }
//        }
//        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                Timber.d("onQueryTextSubmit(query=\"$query\")")
//                if (callingFromActivity) {
//                    Timber.d("Keyboard closing from activity $callingActivity")
//                    hideKeyboard(callingActivity!!)
//                } else {
//                    Timber.d("Keyboard closing from fragment; context = $contextForClosingKeyboard, view = $viewForClosingKeyboard")
//                    hideKeyboardFromFragment(
//                        contextForClosingKeyboard!!,
//                        viewForClosingKeyboard!!
//                    )
//                }
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                Timber.d("onQueryTextChange(newText=\"$newText\")")
//                if(torahFilterableCallback.onSearchChange(newText))
//                    torahFilterableCallback.search(newText ?: "")
//                else Timber.d("search() not called; onSearchChange returned false (onQueryTextChange was probably called by the system not the user)")
//                return false
//            }
//        })
//    }
//
//fun <T, VH : RecyclerView.ViewHolder?> filter(
//        constraints: List<String>,
//        originalList: List<T>,
//        workingList: MutableList<T>,
//        recyclerViewAdapter: RecyclerView.Adapter<VH>?,
//        shiurFilterOption: ShiurFilterOption
//    ) {
//        workingList.clear()
//        for (element in originalList) {
//            for (constraint in constraints) {
//                if (element.getReceiver(shiurFilterOption)
//                        .toLowerCase(Locale.ROOT) == constraint.toLowerCase(Locale.ROOT)
//                        .trim()//i.e. filter condition met
//                ) workingList.add(element)//TODO would it be more efficient to leave the elements in workingList which meet the filter criteria, remove the ones which don't, and add the missing ones, as opposed to clearing all of workingList and adding the ones which meet the criteria? Maybe make another list and set workingList to the union, or something like that?
//            }
//        }
//        recyclerViewAdapter?.notifyDataSetChanged() //TODO make more efficient using [DiffUtil]
//    }
//
//    /**
//     * Used for subcategory page and individual speaker page to add button to menu which says "SHIURIM"
//     * and takes the user to the child shiurim page
//     * */
//    fun setupShiurimButton(
//        menu: Menu?,
//        menuInflater: MenuInflater,
//        packageContext: Context,
//        putExtrasLambda: Intent.() -> Unit
//    ): Boolean {
//        menuInflater.inflate(R.menu.speaker_page, menu)
//        val viewShiurimButtton: MenuItem? = menu?.findItem(R.id.view_shiurim)
//        viewShiurimButtton?.setOnMenuItemClickListener {
//            Toast.makeText(it.actionView.context,
//                "Unimplemented: This button will eventually navigate to BaseShiurimPageActivity (the same page as Recently Added, Favorites, etc) and display the correct shiurim, but this functionality can only be added once the database is implemented.",
//                Toast.LENGTH_LONG
//            ).show()
//            /*val intent = Intent(packageContext, BaseShiurimPageActivity::class.java)
//            intent.apply(putExtrasLambda)
//            packageContext.startActivity(intent)*/
//            true
//        }
//        return true
//    }
//
//    fun setupShiurimButton(
//        menu: Menu?,
//        menuInflater: MenuInflater,
//        packageContext: Context,
//        title: String?,
//        shiurim: ArrayList<Parcelable>,
//        viewToInflateSnackbar: View
//    ) {
//        menuInflater.inflate(R.menu.speaker_page, menu)
//        val viewShiurimButtton: MenuItem? = menu?.findItem(R.id.view_shiurim)
//        viewShiurimButtton?.setOnMenuItemClickListener {
//            Toast.makeText(viewToInflateSnackbar.context,
//                "Unimplemented: This button will eventually navigate to BaseShiurimPageActivity (the same page as Recently Added, Favorites, etc) and display the correct shiurim, but this functionality can only be added once the database is implemented.",
//                Toast.LENGTH_LONG
//            ).show()
//           /* val intent = Intent(packageContext, BaseShiurimPageActivity::class.java)
//            intent.apply {
//                putExtra(CONSTANTS.INTENT_EXTRA_SHIURIM_PAGE_TITLE, title)
//                putParcelableArrayListExtra(
//                    CONSTANTS.INTENT_EXTRA_SHIURIM_PAGE_SHIURIM,
//                    shiurim
//                )
//            }
//            packageContext.startActivity(intent)*/
//            true
//        }
//    }
//
//    fun setupStartSelectionButton(
//        menu: Menu?,
//        dragSelectableActivity: DragSelectableActivity,
//        viewToInflateSnackbar: View
//    ) {
//        val startSelectionButton = menu?.findItem(R.id.start_selection_button)
//        val snackbar = Snackbar.make(
//            viewToInflateSnackbar,
//            "Tap to select, long-tap and drag to multi select",
//            Snackbar.LENGTH_LONG
//        )
//        startSelectionButton?.setOnMenuItemClickListener {
//
//            when (dragSelectableActivity.dragSelectModeEnabled) {
//                false -> {
//                    snackbar.show()
//                    dragSelectableActivity.dragSelectModeEnabled = true
//                    startSelectionButton.icon =
//                        ContextCompat.getDrawable(
//                            mEntireApplicationContext,
//                            R.drawable.ic_cancel_filled
//                        )
//                }
//                true -> {
//                    snackbar.dismiss()
//                    dragSelectableActivity.dragSelectModeEnabled = false
//                    startSelectionButton.icon = ContextCompat.getDrawable(
//                        mEntireApplicationContext,
//                        R.drawable.ic_select_all
//                    )
//                    dragSelectableActivity.clearSelection()
//                }
//            }
//            //TODO should I make a toast which lets the user know that they can long click and drag?
//            // I feel like that won't neccesarily be an easy thing to figure out, and it is one of
//            // the best parts about the feature.
//            true
//        }
//    }
//
//    //TODO partition the input space of this function and write tests:
//    //Letter by letter, and then copy and delete whole search phrase at once?
//    /**
//     * Filters a list (and an optional recycler view) based on a constraint
//     * @param constraint can be a partial phrase from a SearchView or an entry from a [ChooserFastScrollerDialog].
//     * When using a [RangeSlider] to filter by length, constraint must not satisfy String.isEmpty()/must contain a character, but the contents of which will be ignored. TODO should probably make it null instead
//     * @param originalList the list which is displayed when no filter constraints are imposed on the dataset
//     * @param workingList the list being displayed in the recyclerview
//     * @param recyclerViewAdapter if null, notifyDataSetChanged() will not be called (useful for testing)
//     * @param exactMatch  false if filtering/searching from SearchView and results should be partial.
//     * true if filtering from dialog
//     * */
//    fun <T, VH : RecyclerView.ViewHolder?> filter(
//        constraint: String,
//        originalList: List<T>,
//        workingList: MutableList<T>,
//        recyclerViewAdapter: RecyclerView.Adapter<VH>? = null,
//        shiurFilterOption: ShiurFilterOption = ShiurFilterOption.TITLE,
//        exactMatch: Boolean = false,
//        filterWithinPreviousResults: Boolean = false,
//        animation: Boolean = false,
//        intRange: IntRange? = null,
//        shiurFilterOptionToLogResultsBy: ShiurFilterOption = ShiurFilterOption.TITLE,
//        maxNumberOfShiurimToLog: Int = 30
//    ) {
//        Timber.d(
//            "Filter called with parameters: filter(\n" +
//                "        constraint=$constraint,\n" +
//                "        originalList.take(maxNumberOfShiurimToLog)=${originalList.mapToFilterOption(
//                    shiurFilterOptionToLogResultsBy
//                ).take(maxNumberOfShiurimToLog)},\n" +
//                "        workingList.take(maxNumberOfShiurimToLog)=${workingList.mapToFilterOption(
//                    shiurFilterOptionToLogResultsBy
//                ).take(maxNumberOfShiurimToLog)},\n" +
//                "        recyclerViewAdapter=$recyclerViewAdapter,\n" +
//                "        shiurFilterOption=$shiurFilterOption,\n" +
//                "        exactMatch=$exactMatch,\n" +
//                "        filterWithinPreviousResults=$filterWithinPreviousResults,\n" +
//                "        animation=$animation,\n" +
//                "        intRange=$intRange,\n" +
//                "        shiurFilterOptionToLogResultsBy= $shiurFilterOptionToLogResultsBy,\n" +
//                "        maxNumberOfShiurimToLog=$maxNumberOfShiurimToLog"
//        )
//        //TODO Would it make filtering more efficient by using indices instead of full objects? Or are they just references...?
//        if (animation) {
//            if (intRange == null) TODO("Did not implement filter animated to support filtering by length")
//            else filterAnimated(
//                constraint,
//                originalList,
//                workingList,
//                filterWithinPreviousResults,
//                exactMatch,
//                shiurFilterOption,
//                recyclerViewAdapter,
//                intRange
//            )
//        } else {
//            if (constraint.isEmpty()) {
//                reset(originalList,workingList, null as  RecyclerView.Adapter<VH>?)
//            } else {
//                val filterPattern = constraint.toLowerCase(Locale.ROOT).trim()
//                if (filterWithinPreviousResults) filterWithinPreviousResults(
//                    workingList,
//                    filterPattern,
//                    shiurFilterOption,
//                    exactMatch,
//                    intRange
//                )
//                else filterRegular(
//                    originalList,
//                    workingList,
//                    filterPattern,
//                    shiurFilterOption,
//                    exactMatch,
//                    intRange
//                )
//            }
//            recyclerViewAdapter?.notifyDataSetChanged() //TODO make more efficient using [DiffUtil]
//        }
//        Timber.d(
//            "Working List (After mutation, represented only by it.getReceiver(ShiurFilterOption.$shiurFilterOptionToLogResultsBy)).take($maxNumberOfShiurimToLog) = ${
//                workingList.mapToFilterOption(
//                    shiurFilterOptionToLogResultsBy
//                ).take(maxNumberOfShiurimToLog)
//            }"
//        )
//    }
//
//    fun <T> List<T>.mapToFilterOption(filterOption: ShiurFilterOption): List<String> {
//        return map { it.getReceiver(filterOption) }
//    }
//
//    private fun <T> filterWithinPreviousResults(
//        workingList: MutableList<T>,
//        filterPattern: String,
//        shiurFilterOption: ShiurFilterOption,
//        exactMatch: Boolean,
//        intRange: IntRange?
//    ) {
//        val tempWorkingList =
//            workingList.toList()//TODO can this be inlined to be more memory efficient, or will it create a new object every time it goes through the loop and be more memory inefficient?
//        for (element in tempWorkingList) {
//            if (!element.getReceiver(shiurFilterOption).toLowerCase(Locale.ROOT)
//                    .matchesConstraint(
//                        filterPattern,
//                        exactMatch,
//                        intRange
//                    )//i.e. filter condition not met
//            ) workingList.remove(element)
//        }
//    }
//
//    private fun <T> filterRegular(
//        originalList: List<T>,
//        workingList: MutableList<T>,
//        filterPattern: String,
//        shiurFilterOption: ShiurFilterOption,
//        exactMatch: Boolean,
//        intRange: IntRange?
//    ) {
//        workingList.clear()
//        for (element in originalList) {
//            if (element.getReceiver(shiurFilterOption).toLowerCase(Locale.ROOT)
//                    .matchesConstraint(
//                        filterPattern,
//                        exactMatch,
//                        intRange
//                    )//i.e. filter condition met
//            ) workingList.add(element)
//        }
//    }
//
//    private fun <T, VH : RecyclerView.ViewHolder?> filterAnimated(
//        constraint: String,
//        originalList: List<T>,
//        workingList: MutableList<T>,
//        filterWithinPreviousResults: Boolean,
//        exactMatch: Boolean,
//        shiurFilterOption: ShiurFilterOption,
//        recyclerView: RecyclerView.Adapter<VH>?,
//        intRange: IntRange?
//    ) {
//        //not sure how this works, just copying it from the popular SO question about filtering with SearchView
//        if (filterWithinPreviousResults) TODO("animation version of fiter does not currently take into account whether they are searching within previous results (i.e. it ignores it and only filters within the original list), and therefore does not supprt filtering within previous results.")
//        var completeListIndex = 0
//        var filteredListIndex = 0
//        //TODO does not account for wmpty constraint
//        while (completeListIndex < originalList.size) {
//            val element: T = originalList[completeListIndex]
//            val filter: T = workingList[filteredListIndex]
//            val elementReceiver = element.getReceiver(shiurFilterOption)
//            val filterReceiver = filter.getReceiver(shiurFilterOption)//no clue what this does
//            if (elementReceiver.toLowerCase(Locale.ROOT).trim()
//                    .matchesConstraint(constraint, exactMatch, intRange)
//            ) {
//                if (filteredListIndex < workingList.size) {
//                    if (elementReceiver != filterReceiver) {
//                        workingList.add(filteredListIndex, element)
//                        recyclerView?.notifyItemInserted(filteredListIndex)
//                    }
//                } else {
//                    workingList.add(filteredListIndex, element)
//                    recyclerView?.notifyItemInserted(filteredListIndex)
//                }
//                filteredListIndex++
//            } else if (filteredListIndex < workingList.size) {
//                if (elementReceiver == filterReceiver) {
//                    workingList.removeAt(filteredListIndex)
//                    recyclerView?.notifyItemRemoved(filteredListIndex)
//                }
//            }
//            completeListIndex++
//        }
//    }
//
//    fun <T, VH : RecyclerView.ViewHolder?> reset(
//        originalList: List<T>,
//        workingList: MutableList<T>,
//        recyclerView: RecyclerView.Adapter<VH>?
//    ) {
//        //TODO make reset more efficient by using indices?
//        if (workingList.size != 0) workingList.clear()
//        workingList.addAll(originalList)
//        recyclerView?.notifyDataSetChanged()
//    }
//
//    /*Tests:
//    *   val whatTheListShouldBe: MutableList<Shiur> = mutableListOf(
//        Shiur(baseSpeaker = "A", baseLength = "123", baseTitle = "a"),
//        Shiur(baseSpeaker = "A", baseLength = "123", baseTitle = "b"),
//        Shiur(baseSpeaker = "A", baseLength = "123", baseTitle = "c"),
//        Shiur(baseSpeaker = "A", baseLength = "456", baseTitle = "d"),
//        Shiur(baseSpeaker = "A", baseLength = "456", baseTitle = "e"),
//        Shiur(baseSpeaker = "A", baseLength = "456", baseTitle = "f"),
//        Shiur(baseSpeaker = "A", baseLength = "789", baseTitle = "g"),
//        Shiur(baseSpeaker = "A", baseLength = "789", baseTitle = "h"),
//        Shiur(baseSpeaker = "A", baseLength = "789", baseTitle = "i"),
//        Shiur(baseSpeaker = "B", baseLength = "123", baseTitle = "j"),
//        Shiur(baseSpeaker = "B", baseLength = "123", baseTitle = "k"),
//        Shiur(baseSpeaker = "B", baseLength = "123", baseTitle = "l"),
//        Shiur(baseSpeaker = "B", baseLength = "456", baseTitle = "m"),
//        Shiur(baseSpeaker = "B", baseLength = "456", baseTitle = "n"),
//        Shiur(baseSpeaker = "B", baseLength = "456", baseTitle = "o"),
//        Shiur(baseSpeaker = "B", baseLength = "789", baseTitle = "p"),
//        Shiur(baseSpeaker = "B", baseLength = "789", baseTitle = "q"),
//        Shiur(baseSpeaker = "B", baseLength = "789", baseTitle = "r"),
//        Shiur(baseSpeaker = "C", baseLength = "123", baseTitle = "s"),
//        Shiur(baseSpeaker = "C", baseLength = "123", baseTitle = "t"),
//        Shiur(baseSpeaker = "C", baseLength = "123", baseTitle = "u"),
//    )
//    val list: MutableList<Shiur> = mutableListOf(
//        Shiur(baseSpeaker = "A", baseLength = "123", baseTitle = "a"),
//        Shiur(baseSpeaker = "B", baseLength = "789", baseTitle = "r"),
//        Shiur(baseSpeaker = "A", baseLength = "123", baseTitle = "b"),
//        Shiur(baseSpeaker = "A", baseLength = "456", baseTitle = "d"),
//        Shiur(baseSpeaker = "A", baseLength = "456", baseTitle = "e"),
//        Shiur(baseSpeaker = "B", baseLength = "123", baseTitle = "k"),
//        Shiur(baseSpeaker = "B", baseLength = "123", baseTitle = "j"),
//        Shiur(baseSpeaker = "A", baseLength = "123", baseTitle = "c"),
//        Shiur(baseSpeaker = "A", baseLength = "789", baseTitle = "h"),
//        Shiur(baseSpeaker = "A", baseLength = "789", baseTitle = "i"),
//        Shiur(baseSpeaker = "B", baseLength = "789", baseTitle = "p"),
//        Shiur(baseSpeaker = "A", baseLength = "789", baseTitle = "g"),
//        Shiur(baseSpeaker = "B", baseLength = "456", baseTitle = "m"),
//        Shiur(baseSpeaker = "B", baseLength = "456", baseTitle = "n"),
//        Shiur(baseSpeaker = "B", baseLength = "456", baseTitle = "o"),
//        Shiur(baseSpeaker = "B", baseLength = "123", baseTitle = "l"),
//        Shiur(baseSpeaker = "C", baseLength = "123", baseTitle = "u"),
//        Shiur(baseSpeaker = "B", baseLength = "789", baseTitle = "q"),
//        Shiur(baseSpeaker = "C", baseLength = "123", baseTitle = "s"),
//        Shiur(baseSpeaker = "C", baseLength = "123", baseTitle = "t"),
//        Shiur(baseSpeaker = "A", baseLength = "456", baseTitle = "f"),
//    )
//    val recyclerViewDummy: RecyclerView.Adapter<RecyclerView.ViewHolder?>? = null
//    list.sort(
//        recyclerViewDummy,
//        mapOf(
//            ShiurFilterOption.SPEAKER to true,
//            ShiurFilterOption.LENGTH to false,
//            ShiurFilterOption.TITLE to true
//        )
//    )
//    println("copyList.toList():            ${list.toList()}")
//    println("whatTheListShouldBe.toList(): ${whatTheListShouldBe.toList()}")
//    println()
//    println("Copy list: ")
//    for (shiur in list) {
//        println(shiur)
//    }*/
//    /**
//     * Sorts a [RecyclerView] by mutliple [ShiurFilterOption]s
//     * NOTE: mutates the provided list in the process
//    @param recyclerView is nullable so that the function can be tested without having to create an
//    actual recyclerview by passing in null.
//     */
//    @JvmName("sortWithListGivenAsParameters")
//    fun <T : OneOfMyClasses, VH : RecyclerView.ViewHolder?> sort(
//        workingList: MutableList<T>,
//        recyclerView: RecyclerView.Adapter<VH>?,
//        shiurFilterOptions: List<ShiurFilterOption>,
//        ascending: List<Boolean>,
//        shiurFilterOptionToLogResultsBy: ShiurFilterOption = ShiurFilterOption.TITLE,
//        maxNumberOfShiurimToLog: Int = 30
//    ) {
//        Timber.d("Sort called with parameters: sort(\n" +
//            "workingList.take($maxNumberOfShiurimToLog)=${workingList.mapToFilterOption(
//                shiurFilterOptionToLogResultsBy
//            ).take(maxNumberOfShiurimToLog)},\n" +
//            "recyclerView=$recyclerView,\n" +
//            "shiurFilterOptions=$shiurFilterOptions,\n" +
//            "ascending=$ascending\n" +
//            ")"
//        )
//        /*//unoptimized version:
//    val oneOfMyClasses = workingList[0]::class
//    val firstSelector = oneOfMyClasses.getPropertyToSortBy(shiurFilterOptions[0])
//    val compareBy = getComparator(ascending, firstSelector, shiurFilterOptions, oneOfMyClasses)
//    workingList.sortWith(compareBy)*/
//        //optimized version:
//        (workingList[0]::class).let {
//            workingList.sortWith(
//                getComparator(
//                    ascending,
//                    firstSelector = it.getPropertyToSortBy(shiurFilterOptions[0]),
//                    shiurFilterOptions,
//                    it
//                )
//            )
//        }
//        recyclerView?.notifyDataSetChanged()//TODO use androidx.recyclerview.widget.DiffUtil to optimize this call to update only the positions of the elements.
//    }
//
//    /**
//     * Sorts a recyclerview by multiple conditions; a wrapper using maps to the version of [sort] which uses lists
//     * NOTE: mutates the provided list in the process
//    @param recyclerView is nullable so that the function can be tested without having to create an
//    actual recyclerview by passing in null.
//     * @param shiurFilterOptionsMappedToAscending a map of shiur filter condition to whether the
//     * shiur should be sorted by the condition in ascending order (true if ascending).
//     * Implemented as a map because it is easier for the caller to read and understand.
//     * */
//    @JvmName("sortWithListGivenAsParameters")
//    fun <T : OneOfMyClasses, VH : RecyclerView.ViewHolder?> sort(
//        workingList: MutableList<T>,
//        recyclerView: RecyclerView.Adapter<VH>?,
//        shiurFilterOptionsMappedToAscending: Map<ShiurFilterOption, Boolean>,
//        shiurFilterOptionToLogResultsBy: ShiurFilterOption = ShiurFilterOption.TITLE,
//        maxNumberOfShiurimToLog: Int = 30
//    ) {
////        Timber.d("Sort called with parameters: sort(workingList=$workingList,recyclerView=$recyclerView,shiurFilterOptionsMappedToAscending=$shiurFilterOptionsMappedToAscending)")
//        /*//unoptimized version:
//    val shiurFilterOptions: List<ShiurFilterOption> =
//        shiurFilterOptionsMappedToAscending.keys.toList()
//    val ascending: List<Boolean> = shiurFilterOptionsMappedToAscending.values.toList()
//    sort(workingList, recyclerView, shiurFilterOptions, ascending)*/
//        sort(
//            workingList,
//            recyclerView,
//            shiurFilterOptionsMappedToAscending.keys.toList(),
//            shiurFilterOptionsMappedToAscending.values.toList(),
//            shiurFilterOptionToLogResultsBy,
//            maxNumberOfShiurimToLog
//        )
//    }
//
//    @JvmName("sortWithListGivenAsReceiver")
//    fun <T : OneOfMyClasses, VH : RecyclerView.ViewHolder?> MutableList<T>.sort(
//        recyclerView: RecyclerView.Adapter<VH>?,
//        shiurFilterOptions: List<ShiurFilterOption>,
//        ascending: List<Boolean>,
//        shiurFilterOptionToLogResultsBy: ShiurFilterOption = ShiurFilterOption.TITLE,
//        maxNumberOfShiurimToLog: Int = 30
//    ) = sort(
//        this,
//        recyclerView,
//        shiurFilterOptions,
//        ascending,
//        shiurFilterOptionToLogResultsBy,
//        maxNumberOfShiurimToLog
//    )
//
//    @JvmName("sortWithListGivenAsReceiver")
//    fun <T : OneOfMyClasses, VH : RecyclerView.ViewHolder?> MutableList<T>.sort(
//        recyclerView: RecyclerView.Adapter<VH>?,
//        shiurFilterOptionsMappedToAscending: Map<ShiurFilterOption, Boolean>,
//        shiurFilterOptionToLogResultsBy: ShiurFilterOption = ShiurFilterOption.TITLE,
//        maxNumberOfShiurimToLog: Int = 30
//    ) = sort(this,
//        recyclerView,
//        shiurFilterOptionsMappedToAscending,
//        shiurFilterOptionToLogResultsBy,
//        maxNumberOfShiurimToLog)
//
//    /**
//     * Sorts a [RecyclerView] by a single condition
//     * NOTE: mutates the provided list in the process
//    @param recyclerView is nullable so that the function can be tested without having to create an
//    actual recyclerview by passing in null.
//     * */
//    fun <T : OneOfMyClasses, VH : RecyclerView.ViewHolder?> sort(
//        workingList: MutableList<T>,
//        recyclerView: RecyclerView.Adapter<VH>?,
//        shiurFilterOption: ShiurFilterOption,
//        ascending: Boolean,
//        shiurFilterOptionToLogResultsBy: ShiurFilterOption = ShiurFilterOption.TITLE,
//        maxNumberOfShiurimToLog: Int = 30
//    ) {
//        Timber.d("Sort called with parameters: sort(\n" +
//            "workingList.take($maxNumberOfShiurimToLog)=${workingList.mapToFilterOption(
//                shiurFilterOptionToLogResultsBy
//            ).take(maxNumberOfShiurimToLog)},\n" +
//            "recyclerView=$recyclerView,\n" +
//            "shiurFilterOption=$shiurFilterOption,\n" +
//            "ascending=$ascending\n" +
//            ")"
//        )
//        val selector: (T) -> String? = { it.getReceiver(shiurFilterOption) }
//        if (ascending) workingList.sortBy(selector)
//        else workingList.sortByDescending(selector)
//        recyclerView?.notifyDataSetChanged()
//    }
//
//    @JvmName("sortMultipleWithPossibilityOfStringOrInt")
//    fun <T, VH : RecyclerView.ViewHolder?> sort(
//        workingList: MutableList<T>,
//        recyclerView: RecyclerView.Adapter<VH>?,
//        shiurFilterOptions: List<ShiurFilterOption>,
//        ascending: List<Boolean>,
//        shiurFilterOptionToLogResultsBy: ShiurFilterOption = ShiurFilterOption.TITLE,
//        maxNumberOfShiurimToLog: Int = 30
//    ) {
//        Timber.d("Sort called with parameters: sort(\n" +
//            "workingList.take($maxNumberOfShiurimToLog)=${workingList.mapToFilterOption(
//                shiurFilterOptionToLogResultsBy
//            ).take(maxNumberOfShiurimToLog)},\n" +
//            "recyclerView=$recyclerView,\n" +
//            "shiurFilterOptions=$shiurFilterOptions,\n" +
//            "ascending=$ascending\n" +
//            ")"
//        )
//        val firstEntry = workingList[0]!!::class.java
//        when {
//            firstEntry.isAssignableFrom("".javaClass) -> {
//                (workingList as MutableList<String>).apply {
//                    when {
//                        ascending.all { it } -> sort()
//                        ascending.all { !it } -> sortDescending()
//                        else -> {
//                            Timber.e("List of strings was provided and list of ascending/descending was mixed with true and false. Sorting by the first entry in the list.")
//                            if (ascending[0]) sort() else sortDescending()
//                        }
//                    }
//                }
//                recyclerView?.notifyDataSetChanged()
//            }
//            firstEntry.isAssignableFrom((1).javaClass) -> {
//                (workingList as MutableList<Int>).apply {
//                    when {
//                        ascending.all { it } -> sort()
//                        ascending.all { !it } -> sortDescending()
//                        else -> {
//                            Timber.e("List of Ints was provided and list of ascending/descending was mixed with true and false. Sorting by the first entry in the list.")
//                            if (ascending[0]) sort() else sortDescending()
//                        }
//                    }
//                }
//                recyclerView?.notifyDataSetChanged()
//            }
//            firstEntry.isAssignableFrom(OneOfMyClasses::class.java) -> sort(
//                workingList as MutableList<OneOfMyClasses>,
//                recyclerView,
//                shiurFilterOptions,
//                ascending
//            )
//            else -> throw InvalidClassException("The first entry in workingList ($firstEntry) is not a recognized class and cannot be converted to a proper receiver.")
//        }
//    }
//
//    @JvmName("sortSingleWithPossibilityOfStringOrInt")
//    fun <T, VH : RecyclerView.ViewHolder?> sort(
//        workingList: MutableList<T>,
//        recyclerView: RecyclerView.Adapter<VH>?,
//        shiurFilterOption: ShiurFilterOption,
//        ascending: Boolean,
//        shiurFilterOptionToLogResultsBy: ShiurFilterOption = ShiurFilterOption.TITLE,
//        maxNumberOfShiurimToLog: Int = 30
//    ) {
//        Timber.d("Sort called with parameters: sort(\n" +
//            "workingList.take($maxNumberOfShiurimToLog)=${workingList.mapToFilterOption(
//                shiurFilterOptionToLogResultsBy
//            ).take(maxNumberOfShiurimToLog)},\n" +
//            "recyclerView=$recyclerView,\n" +
//            "shiurFilterOption=$shiurFilterOption,\n" +
//            "ascending=$ascending\n" +
//            ")"
//        )
//        val firstEntry = workingList[0]!!::class.java
//        when {
//            firstEntry.isAssignableFrom("".javaClass) -> {
//                (workingList as MutableList<String>).apply {
//                    if (ascending) sort() else sortDescending()
//                }
//                recyclerView?.notifyDataSetChanged()
//            }
//            firstEntry.isAssignableFrom((1).javaClass) -> {
//                (workingList as MutableList<Int>).apply {
//                    if (ascending) sort() else sortDescending()
//                }
//                recyclerView?.notifyDataSetChanged()
//            }
//            firstEntry.isAssignableFrom(OneOfMyClasses::class.java) -> sort(
//                workingList as MutableList<OneOfMyClasses>,
//                recyclerView,
//                shiurFilterOption,
//                ascending
//            )
//            else -> throw InvalidClassException("The first entry in workingList ($firstEntry) is not a recognized class and cannot be converted to a proper receiver.")
//        }
//    }
//
//    /**
//     * Creates the [Comparator] which is used to filter a list of [OneOfMyClasses], e.g. Shiurim, by multiple [ShiurFilterOption]s
//     * Can be thought of as a chain resembling something like
//     * val comparator = compareBy(ShiurFullPage::speaker).thenBy { it.title }.thenByDescending { it.length }.thenByDescending { it.series }.thenBy { it.language }
//     * which will then be fed into list.sortedWith(comparator), except the calls to thenBy() and thenByDescending() will also be passed [KProperty]s
//     * @param firstSelector used to start the chain of comparators with ascending or descending order;
//     * should be the first of the list of conditions to be sorted by. The iteration through the [ShiurFilterOption]s
//     * will continue with the [ShiurFilterOption] after [firstSelector]
//     * */
//    private fun getComparator(
//        ascending: List<Boolean>,
//        firstSelector: KProperty1<OneOfMyClasses, String?>,
//        shiurFilterOptions: List<ShiurFilterOption>,
//        classType: KClass<out OneOfMyClasses>
//    ): Comparator<OneOfMyClasses> {
//        var compareBy =
//            if (ascending[0]) compareBy(firstSelector) else compareByDescending(firstSelector)
//        for (index in 1 until shiurFilterOptions.size) {
//            val shiurFilterOption = shiurFilterOptions[index]
//            val isAscending = ascending[index]
//            val propertyToSortBy = classType.getPropertyToSortBy(shiurFilterOption)
//            compareBy = if (isAscending) compareBy.thenBy(propertyToSortBy)
//            else compareBy.thenByDescending(propertyToSortBy)
//        }
//        return compareBy
//    }
//
//    private fun <T : OneOfMyClasses> KClass<T>.getPropertyToSortBy(
//        shiurFilterOption: ShiurFilterOption
//    ): KProperty1<OneOfMyClasses, String?> = when {
//        this == Speaker::class -> Speaker::name
//        this == ShiurFullPage::class -> when (shiurFilterOption) {
//            ShiurFilterOption.ID -> ShiurFullPage::id
//            ShiurFilterOption.CATEGORY -> ShiurFullPage::category
//            ShiurFilterOption.SERIES -> ShiurFullPage::series
//            ShiurFilterOption.SPEAKER -> ShiurFullPage::speaker
//            ShiurFilterOption.TITLE -> ShiurFullPage::title
//            ShiurFilterOption.HAS_DESCRIPTION -> TODO("Not yet sure how to implement this")
////if (description!!.isBlank()) "no" /*doesn't have a description*/ else "yes" /*does have a description*/ //used yes/no because the user presented options in the dropdown menu for these are yes and no
//            ShiurFilterOption.HAS_ATTACHMENT -> TODO("Not yet sure how to implement this")
////if (attachment!!.isBlank()) "no" /*doesn't have an attachment link*/ else "yes" /*does have an attachment link*/ //^^^
//            ShiurFilterOption.LENGTH -> ShiurFullPage::length
//            ShiurFilterOption.DATE_ADDED_TO_PERSONAL_COLLECTION -> TODO("Not yet sure how to implement this")
//            ShiurFilterOption.DATE_UPLOADED -> ShiurFullPage::uploaded //TODO should probably implement this using date picker
//            ShiurFilterOption.LANGUAGE -> ShiurFullPage::language
//        }
//        this == Shiur::class -> when (shiurFilterOption) {
//            ShiurFilterOption.SPEAKER -> Shiur::baseSpeaker
//            ShiurFilterOption.LENGTH -> Shiur::baseLength
//            ShiurFilterOption.TITLE -> Shiur::baseTitle
//            else -> TODO("Not sure why this would be called; `this` = $this, shiurFilterOption = $shiurFilterOption ")
//        }
//        this == Playlist::class -> Playlist::playlistName
//        this == Category::class -> Category::name //TODO enable sorting by whether has children
//        else -> TODO("Not sure why this would be called; `this` = $this, shiurFilterOption=$shiurFilterOption") /*this as String*/
//    } as KProperty1<OneOfMyClasses, String?>
//
//    fun <T : OneOfMyClasses> T.getReceiver(
//        shiurFilterOption: ShiurFilterOption
//    ): String = when (this) {
//        //TODO What about when the user is filtering for only playlists with e.g. 5 or more shiurim? What about searching for a category or series? add support all search criteria
//        is Speaker -> name
//        is ShiurFullPage -> when (shiurFilterOption) {
//            ShiurFilterOption.ID -> id!!
//            ShiurFilterOption.CATEGORY -> category!!
//            ShiurFilterOption.SERIES -> series!!
//            ShiurFilterOption.SPEAKER -> speaker!!
//            ShiurFilterOption.TITLE -> title!!
//            ShiurFilterOption.HAS_DESCRIPTION -> if (description!!.isBlank()) "no" /*doesn't have a description*/ else "yes" /*does have a description*/ //used yes/no because the user presented options in the dropdown menu for these are yes and no
//            ShiurFilterOption.HAS_ATTACHMENT -> if (attachment!!.isBlank()) "no" /*doesn't have an attachment link*/ else "yes" /*does have an attachment link*/ //^^^
//            ShiurFilterOption.LENGTH -> length!!
//            ShiurFilterOption.DATE_ADDED_TO_PERSONAL_COLLECTION -> TODO("Not yet sure how to implement this")
//            ShiurFilterOption.DATE_UPLOADED -> uploaded!! //TODO should probably implement this using date picker
//            ShiurFilterOption.LANGUAGE -> language!!
//        }
//        is Shiur -> when (shiurFilterOption) {
//            ShiurFilterOption.TITLE -> baseTitle!!
//            ShiurFilterOption.SPEAKER -> baseSpeaker!!
//            ShiurFilterOption.LENGTH -> baseLength!!.toInt().toHrMinSec().formatted(false)
//            ShiurFilterOption.ID -> baseId!!
//            else -> TODO("Not sure why a regular Shiur would be being filtered by $shiurFilterOption")
//        }
//        is Playlist -> playlistName
//        else -> TODO("Not sure why this would be called") /*this as String*/
//    }
//
//    fun <T> T.getReceiver(
//        shiurFilterOption: ShiurFilterOption
//    ): String = when (this) {
//        is String -> this
//        is Int -> this.toString()
//        is OneOfMyClasses -> getReceiver(shiurFilterOption)
//        else -> throw InvalidClassException("`this`($this) is not a recognized class and cannot be converted to a proper receiver.")
//    }
//
//    private fun String.matchesConstraint(constraint: String, exactMatch: Boolean, intRange: IntRange?) =
//        when (exactMatch) {
//            true -> {//request is coming from filter dialog; will only ever be inexact match when searching from SearchView, which should only search through name and possibly description
//                if (this.isDigitsOnly() && intRange != null /*TODO /*make sure this works: */ && shiurFilterOption is ShiurFilterOption.LENGTH*/) this.toInt() in intRange //if it is a number, theoretically the only thing it could be is a length of a shiur
//                else this == constraint
//            }
//            false -> {
//                this.contains(constraint)
//            }
//        }
//
//    fun hideKeyboard(activity: Activity) {
//        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        //Find the currently focused view, so we can grab the correct window token from it.
//        var view = activity.currentFocus
//        //If no view currently has focus, create a new one, just so we can grab a window token from it
//        if (view == null) {
//            view = View(activity)
//        }
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }
//
//    fun hideKeyboardFromFragment(context: Context, view: View) {
//        val imm: InputMethodManager =
//            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }
//
//    fun Resources.getShiurFilterOptionString(name: String) =
//        this.getString(ShiurFilterOption.valueOf(name.toUpperCase(Locale.ROOT)).nameStringResourceId)
//
//    fun ShiurFilterOption.getShiurFilterOptionString(resources: Resources = mEntireApplicationContext.resources) =
//        resources.getString(nameStringResourceId)//TODO is it more efficient for the caller to pass in local resources or to use  mEntireApplicationContext.resources?
//
//    /**
//     * From [com.google.android.material.textfield.DropdownMenuEndIconDelegate]
//     * */
//    private fun addRippleEffectOnOutlinedLayout(
//        editText: AutoCompleteTextView,
//        rippleColor: Int,
//        states: Array<IntArray>,
//        boxBackground: MaterialShapeDrawable
//    ) {
//        //TODO to implement
//        val editTextBackground: LayerDrawable
//        val surfaceColor = MaterialColors.getColor(editText, R.attr.colorSurface)
//        val rippleBackground = MaterialShapeDrawable(boxBackground.shapeAppearanceModel)
//        val pressedBackgroundColor = MaterialColors.layer(rippleColor, surfaceColor, 0.1f)
//        val rippleBackgroundColors = intArrayOf(pressedBackgroundColor, Color.TRANSPARENT)
//        rippleBackground.fillColor = ColorStateList(states, rippleBackgroundColors)
//        editTextBackground = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            rippleBackground.setTint(surfaceColor)
//            val colors = intArrayOf(pressedBackgroundColor, surfaceColor)
//            val rippleColorStateList = ColorStateList(states, colors)
//            val mask = MaterialShapeDrawable(boxBackground.shapeAppearanceModel)
//            mask.setTint(Color.WHITE)
//            val rippleDrawable: Drawable =
//                RippleDrawable(rippleColorStateList, rippleBackground, mask)
//            val layers = arrayOf(rippleDrawable, boxBackground)
//            LayerDrawable(layers)
//        } else {
//            val layers = arrayOf<Drawable>(rippleBackground, boxBackground)
//            LayerDrawable(layers)
//        }
//        ViewCompat.setBackground(editText, editTextBackground)
//    }
//
//    fun getLanguage(shiur: Shiur):String{
//        return if(shiur is ShiurFullPage) shiur.language!! else ""//TODO request language from server
//    }
//
//    fun getCategory(shiur: Shiur): Category {
//        return when (shiur) {
//            is ShiurFullPage -> Category(name = shiur.category!!) //TODO should this just be a string?
//            is ShiurSpeakerPage -> Category(name = shiur.Category)
//            is ShiurCategoryPage -> Category(name = "TEST")//TODO should be able to get the category because it is coming from a category page, but it is currently not one of the data class parameters
//            else -> Category() //TODO request series info from server
//        }
//    }
//
//    fun getSeries(shiur: Shiur): String {
//        return when (shiur) {
//            is ShiurFullPage -> shiur.series!!
//            is ShiurQuickSeriesShiurPage -> shiur.Series
//            else -> "TEST" //TODO request series info from server
//        }
//    }
//
//    fun getSpeaker(baseSpeaker: String?): Speaker {
//        //TODO to implement
//        return listOfAllSpeakersFromJson().find { it.name == baseSpeaker } ?: Speaker(name = baseSpeaker ?: "TEST")
//    }
//
//    fun getShiurSize(shiur: ShiurFullPage): String {
//        //TODO to implement
//        return "99.99 MB"
//    }
//
//    /**
//     * Used in testing for loading local JSON or text files into a StringBuilder
//     * */
//    fun loadData(inFile: Int): StringBuilder {
//        var tContents: String? = ""
//
//        val stringBuilder = StringBuilder()
//        val isa: InputStream = mEntireApplicationContext.resources.openRawResource(inFile)
//        val reader = BufferedReader(InputStreamReader(isa))
//        while (true) {
//            try {
//                tContents = reader.readLine()
//                if (tContents == null) break
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//            stringBuilder.appendLine(tContents)
//        }
////        Timber.v("File Contents: $stringBuilder")
//        return stringBuilder
//    }
//
//    fun getPXFromDP(yourdpmeasure: Float): Int = TypedValue
//        .applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            yourdpmeasure,
//            mEntireApplicationContext.resources.displayMetrics
//        ).toInt()
//    fun isShiurFilterOption(string:String) = isInEnum(string, ShiurFilterOption::class.java)
//
//}
/**
 * Used in testing for loading local JSON or text files into a StringBuilder
 * */
fun loadData(inFile: Int): StringBuilder {
    var tContents: String? = ""

    val stringBuilder = StringBuilder()
    val isa: InputStream = mEntireApplicationContext.resources.openRawResource(inFile)
    val reader = BufferedReader(InputStreamReader(isa))
    while (true) {
        try {
            tContents = reader.readLine()
            if (tContents == null) break
        } catch (e: IOException) {
            e.printStackTrace()
        }
        stringBuilder.appendLine(tContents)
    }
//        Timber.v("File Contents: $stringBuilder")
    return stringBuilder
}