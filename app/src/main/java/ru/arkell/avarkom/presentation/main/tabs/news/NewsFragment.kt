package ru.arkell.avarkom.presentation.main.tabs.news

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_news.newsCoordinatorContainer
import kotlinx.android.synthetic.main.fragment_news.newsList
import kotlinx.android.synthetic.main.fragment_news.newsProgressBar
import kotlinx.android.synthetic.main.fragment_news.newsSwipeContainer
import kotlinx.android.synthetic.main.news_item.view.postDate
import kotlinx.android.synthetic.main.news_item.view.postImage
import kotlinx.android.synthetic.main.news_item.view.postText
import kotlinx.android.synthetic.main.news_item.view.postTitle
import ru.arkell.avarkom.R
import ru.arkell.avarkom.data.response.news.NewsItem
import ru.arkell.avarkom.extensions.getAvarkomApplication
import ru.arkell.avarkom.extensions.hide
import ru.arkell.avarkom.extensions.show
import ru.arkell.avarkom.extensions.showSnackbar
import ru.arkell.avarkom.presentation.base.BaseFragment
import ru.arkell.avarkom.presentation.base.BaseRecyclerAdapter
import ru.arkell.avarkom.presentation.main.tabs.news.di.DaggerNewsScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.news.di.NewsScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.news.di.NewsScreenModule
import javax.inject.Inject

class NewsFragment : BaseFragment(), NewsView, SwipeRefreshLayout.OnRefreshListener {
  private lateinit var component: NewsScreenComponent
  private val adapter: NewsAdapter by lazy { NewsAdapter() }

  @Inject
  lateinit var presenter: NewsPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_news, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.onAttach(this)
    initViewElements()
    presenter.getNews()
  }

  override fun onDetach() {
    super.onDetach()
    presenter.onDetach()
  }

  override fun initViewElements() {
    newsSwipeContainer.setOnRefreshListener(this)
  }


  override fun initComponent() {
    component = DaggerNewsScreenComponent.builder()
        .persistenceComponent(context.getAvarkomApplication().persistenceComponent())
        .newsScreenModule(NewsScreenModule())
        .build()

    component.inject(this)
  }

  override fun showNews(news: List<NewsItem>) {
    adapter.addData(news)
    newsList.adapter = adapter
  }

  override fun showLoading() {
    newsSwipeContainer.hide(true)
    newsProgressBar.show()
  }

  override fun hideLoading() {
    newsSwipeContainer.isRefreshing = false
    newsSwipeContainer.show()
    newsProgressBar.hide(true)
  }

  override fun showError() {
    newsCoordinatorContainer.showSnackbar("Network error")
  }

  override fun onRefresh() {
    presenter.getNews()
  }

  class NewsAdapter : BaseRecyclerAdapter<NewsItem>() {
    var onItemSelectAction: PublishSubject<NewsItem> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup,
        viewType: Int): RecyclerViewHolder<NewsItem> {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
      return ViewHolder(view)
    }

    private inner class ViewHolder(itemView: View) : RecyclerViewHolder<NewsItem>(itemView) {

      override fun setItem(item: NewsItem, position: Int) {
        itemView.postTitle.text = item.title
        itemView.postDate.setReferenceTime(System.currentTimeMillis())
        itemView.postText.text = item.text
        if (!item.imageLink.isNullOrEmpty())
          Picasso.with(itemView.context).load(item.imageLink).into(itemView.postImage)

      }

    }
  }
}