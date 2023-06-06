package com.example.superhero

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.superhero.data.HeroesRepository
import com.example.superhero.model.Hero
import com.example.superhero.ui.theme.SuperheroTheme
import javax.sql.DataSource

@Composable
fun SuperheroCard (hero: Hero, modifier : Modifier = Modifier){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)

        ) {
       Row(
           verticalAlignment = Alignment.CenterVertically,
           modifier = Modifier
               .padding(16.dp)
               .height(120.dp)) {
           Column(modifier = Modifier.weight(1f)) {
               Text(text = stringResource(id = hero.nameRes), style = MaterialTheme.typography.displaySmall)
               Text(text = stringResource(id = hero.descriptionRes), style = MaterialTheme.typography.bodyLarge)
           }
           Spacer(modifier = Modifier.width(8.dp))
           Box (modifier = Modifier.height(72.dp)){
               Image(
                   painter = painterResource(id = hero.imageRes),
                   contentDescription = "",
                   modifier = Modifier
                       .clip(MaterialTheme.shapes.small)
               )
           }
       } 
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SuperheroList(heroList: List<Hero> ,modifier: Modifier = Modifier, paddingParam : PaddingValues){
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = spring(dampingRatio = DampingRatioLowBouncy)),
        exit = fadeOut(),
        modifier = modifier
        ) {

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            contentPadding = paddingParam,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            itemsIndexed (heroList){
                    index , hero->
                SuperheroCard(
                    hero = hero,
                    modifier = modifier
                        .animateEnterExit(
                            enter = slideInVertically(
                                animationSpec = spring(
                                    stiffness = StiffnessVeryLow,
                                    dampingRatio = DampingRatioLowBouncy),
                                initialOffsetY = {it* (index +1)}
                            )
                        )
                )
            }
        }
    }
}
        



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperheroTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "", modifier = Modifier
                        .weight(1f)
                        .size(72.dp)
                )
                Text(text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayLarge, modifier = Modifier.weight(2f)

                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperheroApp () {
    Scaffold (topBar = { SuperheroTopBar() }) {
        SuperheroList(heroList = HeroesRepository.heroes , paddingParam = it)
    }
}


@Preview("Light Theme")
@Composable
fun PreviewScreenLight(){
    SuperheroTheme (darkTheme = false){
        SuperheroApp()
    }
}

@Preview("Dark Theme")
@Composable
fun PreviewScreenDark(){
    SuperheroTheme (darkTheme = true){
        SuperheroApp()
    }
}