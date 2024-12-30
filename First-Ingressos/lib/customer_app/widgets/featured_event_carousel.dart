import 'package:flutter/material.dart';
import 'package:flutter_carousel_widget/flutter_carousel_widget.dart';
import '../../backend/supabase/supabase.dart';
import '../../backend/schema/events_record.dart';
import 'carousel_config.dart';

class FeaturedEventCarousel extends StatefulWidget {
  const FeaturedEventCarousel({Key? key}) : super(key: key);

  @override
  State<FeaturedEventCarousel> createState() => _FeaturedEventCarouselState();
}

class _FeaturedEventCarouselState extends State<FeaturedEventCarousel> {
  int _currentIndex = 0;
  String? _errorMessage;

  Future<List<EventsRecord>> _fetchFeaturedEvents() async {
    try {
      final response = await SupaFlow.client
          .from('events')
          .select()
          .order('created_at', ascending: false)
          .limit(5);

      final data = response as List;
      return data.map((json) => EventsRecord.fromJson(json as Map<String, dynamic>)).toList();
    } catch (e) {
      print('Erro ao buscar eventos em destaque: $e');
      setState(() {
        _errorMessage = 'Erro ao buscar eventos em destaque: $e';
      });
      return [];
    }
  }

  @override
  Widget build(BuildContext context) {
    final height = CarouselConfig.getHeight(context);

    return FutureBuilder<List<EventsRecord>>(
      future: _fetchFeaturedEvents(),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return SizedBox(
            height: height,
            child: const Center(child: CircularProgressIndicator()),
          );
        }

        if (snapshot.hasError || _errorMessage != null) {
          return SizedBox(
            height: height,
            child: Center(
              child: Text(
                _errorMessage ?? 'Erro ao carregar eventos em destaque',
                style: const TextStyle(color: Colors.red),
              ),
            ),
          );
        }

        final events = snapshot.data ?? [];

        if (events.isEmpty) {
          return SizedBox(
            height: height,
            child: const Center(
              child: Text('Nenhum evento em destaque'),
            ),
          );
        }

        return Column(
          children: [
            FlutterCarousel(
              items: events.map((event) {
                return Container(
                  margin: const EdgeInsets.symmetric(horizontal: 5),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(20),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withOpacity(0.2),
                        spreadRadius: 1,
                        blurRadius: 10,
                        offset: const Offset(0, 3),
                      ),
                    ],
                  ),
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(20),
                    child: Stack(
                      fit: StackFit.expand,
                      children: [
                        Image.asset(
                          'assets/images/istockphoto-1181169459-612x612.jpg',
                          fit: BoxFit.cover,
                        ),
                        Container(
                          decoration: BoxDecoration(
                            gradient: LinearGradient(
                              begin: Alignment.topCenter,
                              end: Alignment.bottomCenter,
                              colors: [
                                Colors.transparent,
                                Colors.black.withOpacity(0.7),
                              ],
                            ),
                          ),
                        ),
                        Positioned(
                          bottom: 20,
                          left: 20,
                          right: 20,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Text(
                                event.title ?? 'Sem título',
                                style: const TextStyle(
                                  color: Colors.white,
                                  fontSize: 24,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                              const SizedBox(height: 8),
                              Text(
                                event.location ?? 'Local não definido',
                                style: const TextStyle(
                                  color: Colors.white70,
                                  fontSize: 16,
                                ),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                'R\$ ${event.price?.toStringAsFixed(2) ?? '0.00'}',
                                style: const TextStyle(
                                  color: Colors.white,
                                  fontSize: 20,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                );
              }).toList(),
              options: CarouselOptions(
                height: height,
                viewportFraction: CarouselConfig.getViewportFraction(context),
                enlargeCenterPage: true,
                autoPlay: true,
                autoPlayInterval: CarouselConfig.autoPlayInterval,
                autoPlayAnimationDuration: CarouselConfig.animationDuration,
                autoPlayCurve: CarouselConfig.animationCurve,
                onPageChanged: (index, reason) {
                  setState(() {
                    _currentIndex = index;
                  });
                },
                showIndicator: true,
                slideIndicator: CircularSlideIndicator(),
              ),
            ),
          ],
        );
      },
    );
  }
}
