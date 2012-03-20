Secure Airport Tower
====================

Objectifs du cours
------------------

Les objectifs de ce cours d'informatique sont:

-	Le rappel des concepts de base de la programmation : variables, expressions,
	structures de contrôle, entrées-sorties, fonctions, structures de données. 
-	L'introduction et la mise en pratique des notions de programmation par
	objets (les objets, l'encapsulation, les classes, l'héritage,
	le polymorphisme). 
-	La mise en pratique de ces concepts par la conception de programmes
	(spécification, résolution algorithmique, programmation, test).
-	La prise en main d'un certain nombre d'outils d'aide au développement de
	programmes (dévermineurs, compilation séparée etc...).
-	La mise en place d’une démarche d’ingénieur pour trouver une solution
	intelligente à un problème réel.

Les concepts théoriques introduits lors des cours magistraux seront mis en
pratique dans le cadre d'exercices sur machines et par le biais de la
réalisation d'un petit projet en équipe. 

Scénario du projet
------------------

Il s’agit de développer un système de communication sécurisé entre une tour de
contrôle d’un aéroport et une multitude d’avions qui communiquent avec la tour.

La tour de contrôle est un système informatique critique qui se doit d’être
fiable, sécurisé, performant et intelligent afin de ne pas mettre en danger la
vie de plusieurs milliers de personnes.

Vous trouverez ci-dessous les besoins qui ont été exprimés par les utilisateurs
finaux.


Spécifications envoyées par la Direction Générale de l’Aéroport
---------------------------------------------------------------

La première mission de la tour de contrôle est de pouvoir échanger des messages
avec un avion afin de lui donner des informations / instructions et de recevoir
des informations.

Malheureusement, l’industrie aéronautique a été frappée à des nombreuses
reprises par des attaques terroristes, i.e. le vol TWA 847 et le vol PanAm 103.
Et depuis les attaques du 11 Septembre en Amérique, les exigences sécuritaires
sont encore plus fortes. Dès lors, nous exigeons que toutes les communications
soient cryptées.

http://fr.wikipedia.org/wiki/Vol_TWA_847
http://fr.wikipedia.org/wiki/Vol_103_Pan_Am  

Nous disposons de personnel spécialement entrainé au traitement des situations
de crise qui scrute en permanence un écran sur lequel il peut voir la position
actuelle des avions sur une carte et un journal d’événements où il voit passer
des messages échangés avec l’avion.

Le programme de la tour de contrôle est capable de communiquer de manière
totalement automatisée avec les avions et l’intervention humaine n’est requise
qu’en cas de crise.

Cette communication automatisée est possible car les messages échangés suivent
un protocole international qui définit les types de messages qui peuvent être
échangés entre une tour de contrôle et un avion (Hello, RoutingMessage,
LandingRequest, MayDay, etc). 

Un aéroport comme le notre voit passer chaque jour des centaines d’avions. Par
conséquent, nous souhaitons que le système soit capable de gérer des
communications avec plusieurs avions à la fois. Et que nos contrôleurs aériens
puissent prendre connaissance de toutes ces communications parallèles
instantanément.

Une fois qu’un avion s’annonce à la tour de contrôle, celle-ci lui envoie
automatiquement des instructions de routage et les met en quelque sorte sur une
liste d’attente tant que la piste d’atterrissage n’est pas libre. 

Dans notre cas, la plupart du temps, la tour de contrôle accorde une
autorisation d’atterrissage dans l’ordre où elle a reçu les demandes.
Toutefois, il arrive parfois qu’un avion se trouve en difficulté dans les airs
(manque de kérosène, moteur en feu). Cela déclenche immédiatement une situation
de crise qui entraine que l’on doive rerouter tous les avions pour permettre à
l’avion en difficulté d’atterrir le plus tôt possible et éviter ainsi une
catastrophe aérienne.

La sauvegarde des vies humaines est la priorité numéro un de notre tour de 
contrôle, dès lors nous souhaiterions que tout soit mis en œuvre pour que
l’échange des messages d’urgence soient traités dans les meilleurs délais.
En effet, les avions envoient des messages dont l’importance est variable et
il est souhaitable que la tour gère les messages selon l’ordre de priorité de
ces derniers.

De même, nous souhaiterions disposer d’une système intelligent qui fera un
routage optimum des avions pour réduire au minimum le risque de perte de vies
humaines. Ainsi, nous aimerions constituer divers scénarii de crise afin de
pouvoir tester vos algorithmes et pouvoir mesurer le nombre de pertes humaines
que l’on souhaite voir tendre vers zéro.

Concernant les délais de réalisation, nous devons vous annoncer que nous avons
absolument besoin du nouveau logiciel de la tour de contrôle avant le mois de
juillet de cette année. En effet, nous prévoyons une forte croissance du trafic
aérien pour les jeux olympiques de Londres et notre logiciel actuel ne pourra
pas tenir la charge.

Etant donné les délais extrêmement courts, les contraintes de sécurité, la
complexité du système et le nombre de vies humaines qui doivent être
préservées, nous sommes convaincus que seuls les étudiants de premières année
IC sont en mesure de satisfaire les objectifs de cette mission.
In IC, we trust !
